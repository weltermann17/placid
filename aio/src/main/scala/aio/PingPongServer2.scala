package aio

import java.net.{ InetSocketAddress, StandardSocketOptions }
import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousServerSocketChannel, AsynchronousSocketChannel, CompletionHandler }

import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

import buffer.{ ByteBufferPool, defaultCapacity }
import concurrent.Implicits.{ globalasynchronouschannelgroup, globalexecutioncontext }
import conduit._

/**
 *
 */
final class PingPongServer2 {

  private[this] final val server = ServerSocketChannelConduit(new InetSocketAddress("127.0.0.1", 8080), 10000)

  println(s"server2 started : $server")
  def x: Future[Unit] = for {
    c ← server.read
    _ ← { println(c); h(c); x }
  } yield ()
  def h(c: SocketChannelConduit): Future[Unit] = for {
    buffer ← c.read
    _ ← { respond(buffer); c.write(buffer) }
    _ ← h(c)
  } yield ()
  x

  @inline private[this] final def respond(buffer: ByteBuffer) = { buffer.clear; buffer.put(response); buffer.flip }

  private[this] final val constant = 48

  private[this] final val response: Array[Byte] = {
    val txt = """HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: text/plain
Content-Length: 4
Date: Wed, 11 Mar 2015 13:13:24 GMT

pong""".getBytes
    val buf = ByteBufferPool.acquire(defaultCapacity)
    for (_ ← 1 to constant) buf.put(txt)
    buf.flip
    val arr = new Array[Byte](buf.remaining)
    buf.get(arr, 0, arr.length)
    ByteBufferPool.release(buf)
    arr
  }

}

object PingPongServer2 extends App {

  ByteBufferPool.create(defaultCapacity, 1000, true)

  new PingPongServer2

  Thread.sleep(10 * 60 * 1000)

}

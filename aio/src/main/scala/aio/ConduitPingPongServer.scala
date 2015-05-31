package aio

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import scala.concurrent.Future

import aio.buffer.ByteResult.asByteBuffer
import buffer.{ ByteResult, ByteBufferPool, defaultCapacity }
import concurrent.Implicits.globalexecutioncontext
import conduit.{ ServerSocketConduit, SocketConduit, loop }
import conduit.FileConduit.{ forWriting, forReading, string2path }

/**
 *
 */
final class ConduitPingPongServer {

  def run = loop { server.read map { c ⇒ loop { c.read flatMap { b ⇒ c.write(respond(b)) } } } }

  private[this] final val server = ServerSocketConduit(new InetSocketAddress("127.0.0.1", 8080))

  private[this] final def respond(b: ByteResult): ByteResult = { if (0 < b.capacity) { b.clear; b.put(response); b.flip }; b }

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

/**
 *
 */
object ConduitPingPongServer

    extends App {

  ByteBufferPool.create(defaultCapacity, 1000, true)
  val server = new ConduitPingPongServer
  server.run
  Thread.sleep(10 * 60 * 1000)

}

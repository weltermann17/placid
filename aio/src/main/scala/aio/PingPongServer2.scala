package aio

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import scala.concurrent.Future

import aio.buffer.ByteResult.asByteBuffer
import buffer.{ ByteBufferPool, defaultCapacity }
import concurrent.Implicits.globalexecutioncontext
import conduit.{ ServerSocketChannelConduit, SocketChannelConduit, loop }
import conduit.FileConduit.{ forWriting, forReading, string2path }

/**
 *
 */
final class PingPongServer2 {

  private[this] final val server = ServerSocketChannelConduit(new InetSocketAddress("127.0.0.1", 8080))

  {
    println(s"server2 started : $server")
    if (true) {
      def x: Future[Unit] = for {
        c ← server.read
        _ ← { h(c); x }
      } yield ()
      def h(c: SocketChannelConduit): Future[Unit] = for {
        byteresult ← c.read
        _ ← { respond(byteresult); c.write(byteresult) }
        _ ← h(c)
      } yield ()
      x
    } else {
      def h(c: SocketChannelConduit): Future[Unit] = for {
        byteresult ← c.read
        _ ← { println(byteresult); respond(byteresult); c.write(byteresult) }
        _ ← h(c)
      } yield ()
      for {
        _ ← loop {
          for {
            c ← server.read
            _ ← h(c)
          } yield ()
        }
      } yield ()
    }
  }

  private[this] final def respond(buffer: ByteBuffer) = if (0 < buffer.capacity) { buffer.clear; buffer.put(response); buffer.flip }

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

  for {
    in ← forReading("/tmp/test.in")
    out ← forWriting("/tmp/test.out")
    _ ← loop {
      for {
        b ← in.read
        _ ← out.write(b)
      } yield ()
    }
  } yield ()

  Thread.sleep(10 * 60 * 1000)

}

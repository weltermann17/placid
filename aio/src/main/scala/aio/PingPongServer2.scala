package aio

import java.net.{ InetSocketAddress, StandardSocketOptions }
import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousServerSocketChannel, AsynchronousSocketChannel, CompletionHandler }

import scala.util.{ Failure, Success, Try }

import buffer.{ defaultCapacity, RichByteBuffer, ByteBufferPool }
import concurrent.Implicits.{ globalasynchronouschannelgroup, globalexecutioncontext }
import conduit.SocketChannelConduit

/**
 *
 */
final class PingPongServer2 {

  private[this] final val server = AsynchronousServerSocketChannel
    .open(globalasynchronouschannelgroup)
    .setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.box(true))
    .setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(defaultCapacity))
    .bind(new InetSocketAddress("127.0.0.1", 8080), 10000)

  println(s"server started : $server")
  server.accept(null: Null, accepthandler)

  private[this] object accepthandler extends CompletionHandler[AsynchronousSocketChannel, Null] {

    @inline def failed(e: Throwable, a: Null) = println(s"accept failed : $e")

    @inline def completed(socket: AsynchronousSocketChannel, a: Null) = {

      server.accept(null: Null, accepthandler)
      println(s"accept : $socket")

      val s = SocketChannelConduit(socket)
      def handler: Try[ByteBuffer] ⇒ Unit = {
        case Failure(e) ⇒ println(s"read failed : $e")
        case Success(buffer) ⇒
          if (buffer.remaining == 40 * constant) {
            val writebuffer = ByteBufferPool.acquire(defaultCapacity)
            writebuffer.put(response)
            writebuffer.flip
            s.write(writebuffer).onComplete {
              case Failure(e) ⇒ println(s"write failed : $e")
              case Success(()) ⇒ s.read.onComplete(handler)
            }
          } else {
            socket.close
          }
      }
      s.read.onComplete(handler)
    }

  }

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
    arr
  }

}

object PingPongServer2 extends App {

  ByteBufferPool.create(defaultCapacity, 10000, true)

  new PingPongServer2

  Thread.sleep(10 * 60 * 1000)

}

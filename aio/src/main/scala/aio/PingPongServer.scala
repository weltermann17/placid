package aio

import java.net.{ InetSocketAddress, StandardSocketOptions }
import java.net.StandardSocketOptions.{ SO_KEEPALIVE, SO_RCVBUF, SO_REUSEADDR, SO_SNDBUF, TCP_NODELAY }
import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousChannelGroup, AsynchronousServerSocketChannel, AsynchronousSocketChannel, CompletionHandler }
import java.util.Collections
import java.util.concurrent.{ AbstractExecutorService, TimeUnit, Executors }

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContextExecutorService

object ExecutionContextExecutorServiceBridge {
  def apply(ec: ExecutionContext): ExecutionContextExecutorService = ec match {
    case null ⇒ throw null
    case eces: ExecutionContextExecutorService ⇒ eces
    case other ⇒ new AbstractExecutorService with ExecutionContextExecutorService {
      @inline override def prepare(): ExecutionContext = other
      @inline override def isShutdown = false
      @inline override def isTerminated = false
      @inline override def shutdown() = ()
      @inline override def shutdownNow() = Collections.emptyList[Runnable]
      @inline override def execute(runnable: Runnable): Unit = other execute runnable
      @inline override def reportFailure(t: Throwable): Unit = other reportFailure t
      @inline override def awaitTermination(length: Long, unit: TimeUnit): Boolean = false
    }
  }
}

final class PingPongServer {

  private[this] final val context = ExecutionContextExecutorServiceBridge(global)

  private[this] final val server = AsynchronousServerSocketChannel
    .open(AsynchronousChannelGroup.withThreadPool(context))
    .setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.box(true))
    .setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(64 * 1024))
    .bind(new InetSocketAddress("127.0.0.1", 8080), 10000)

  println(s"server started : $server")
  server.accept(null: Null, accepthandler)

  private[this] object accepthandler extends CompletionHandler[AsynchronousSocketChannel, Null] {

    @inline def failed(e: Throwable, a: Null) = println(s"accept failed : $e")

    @inline def completed(socket: AsynchronousSocketChannel, a: Null) = {

      server.accept(null: Null, accepthandler)

      socket.setOption(TCP_NODELAY, Boolean.box(true))
      socket.setOption(SO_REUSEADDR, Boolean.box(true))
      socket.setOption(SO_KEEPALIVE, Boolean.box(false))
      socket.setOption(SO_RCVBUF, Integer.valueOf(64 * 1024))
      socket.setOption(SO_SNDBUF, Integer.valueOf(64 * 1024))

      println(s"accept : $socket")

      val readbuffer = ByteBuffer.allocateDirect(64 * 1024)

      val writebuffer = response.duplicate

      object readhandler extends CompletionHandler[Integer, Null] {

        @inline def failed(e: Throwable, a: Null) = println(s"read failed : $e")

        @inline def completed(processed: Integer, a: Null) = {

          if (1920 == processed.intValue) {
            writebuffer.rewind
            socket.write(writebuffer, null: Null, writehandler)
          } else {
            socket.close
            println(s"read only : $processed")
            println(s"closed : $socket")
          }

        }

      }

      object writehandler extends CompletionHandler[Integer, Null] {

        @inline def failed(e: Throwable, a: Null) = println(s"write failed : $e")

        @inline def completed(processed: Integer, a: Null) = {

          readbuffer.clear
          socket.read(readbuffer, null: Null, readhandler)

        }

      }

      socket.read(readbuffer, null: Null, readhandler)

    }

  }

  private[this] final val response: ByteBuffer = {
    val txt = """HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: text/plain
Content-Length: 4
Date: Wed, 11 Mar 2015 13:13:24 GMT

pong""".getBytes
    val buf = ByteBuffer.allocateDirect(64 * 1024)
    for (_ ← 1 to 48) buf.put(txt)
    buf.flip
    buf
  }

}

object PingPongServer extends App {

  new PingPongServer

  Thread.sleep(10 * 60 * 1000)

}

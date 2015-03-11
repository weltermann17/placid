package aio

import java.net.{ InetSocketAddress, StandardSocketOptions }

import java.nio.channels._
import java.nio.charset._
import java.nio._

class PingPongServer {

  val server = AsynchronousServerSocketChannel
    .open
    .setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.box(true))
    .setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(64 * 1024))
    .bind(new InetSocketAddress("localhost", 8080), 10000)

  println(s"server started : $server")
  server.accept(null: Null, accepthandler)

  object accepthandler extends CompletionHandler[AsynchronousSocketChannel, Null] {

    def failed(e: Throwable, a: Null) = println(s"accept failed : $e")

    def completed(socket: AsynchronousSocketChannel, a: Null) = {

      println(s"accept : $socket")

      server.accept(null: Null, accepthandler)

      val buffer = ByteBuffer.allocateDirect(64 * 1204)

      type Handler = CompletionHandler[Integer, Null]

      object readhandler extends CompletionHandler[Integer, Null] {

        def failed(e: Throwable, a: Null) = println(s"read failed : $e")

        def completed(processed: Integer, a: Null) = {

          if (0 < processed.intValue)
            socket.write(response.duplicate(), null: Null, writehandler)
          else {
            socket.close
            println(s"closed : $socket")
          }

        }

      }

      object writehandler extends CompletionHandler[Integer, Null] {

        def failed(e: Throwable, a: Null) = println(s"write failed : $e")

        def completed(processed: Integer, a: Null) = {

          buffer.clear
          socket.read(buffer, null: Null, readhandler)

        }

      }

      socket.read(buffer, null: Null, readhandler)

    }

  }

  val response: ByteBuffer = {
    val txt = """HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: text/plain
Content-Length: 4
Date: Wed, 11 Mar 2015 13:13:24 GMT

pong""".getBytes
    val buf = ByteBuffer.allocateDirect(200)
    buf.put(txt)
    buf.flip
    buf
  }

}

object PingPongServer extends App {

  new PingPongServer

  Thread.sleep(10 * 60 * 1000)

}

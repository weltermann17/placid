package aio
package conduit

import java.net.{ InetSocketAddress, StandardSocketOptions }
import java.nio.channels.{ AsynchronousServerSocketChannel ⇒ ServerSocketChannel, AsynchronousSocketChannel ⇒ SocketChannel, CompletionHandler ⇒ Handler }

import scala.concurrent.{ Future, Promise }

import concurrent.Implicits.globalasynchronouschannelgroup

/**
 *
 */
final class ServerSocketChannelConduit private (

  protected[this] final val server: ServerSocketChannel)

    extends SourceConduit[SocketChannelConduit] {

  final def read: Future[SocketChannelConduit] = {
    val promise = Promise[SocketChannelConduit]
    object accepthandler extends Handler[SocketChannel, Null] {
      @inline def failed(e: Throwable, a: Null) = {
        promise.tryFailure(e)
      }
      @inline def completed(socket: SocketChannel, a: Null) = {
        println(s"accepted : $socket")
        promise.trySuccess(SocketChannelConduit(socket))
      }
    }
    server.accept(null: Null, accepthandler)
    promise.future
  }

}

/**
 *
 */
object ServerSocketChannelConduit {

  def apply(address: InetSocketAddress, backlog: Int = 0): ServerSocketChannelConduit = apply(
    ServerSocketChannel
      .open(globalasynchronouschannelgroup)
      .setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.box(true))
      .bind(address, backlog))

  def apply(server: ServerSocketChannel): ServerSocketChannelConduit = new ServerSocketChannelConduit(server)

}

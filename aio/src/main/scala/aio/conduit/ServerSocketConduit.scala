package aio
package conduit

import java.net.{ InetSocketAddress, StandardSocketOptions }
import java.nio.channels.{ AsynchronousServerSocketChannel ⇒ ServerSocket, AsynchronousSocketChannel ⇒ Socket, CompletionHandler ⇒ Handler }

import scala.concurrent.{ Future, Promise }

import concurrent.Implicits.globalasynchronouschannelgroup

/**
 * Only a SourceConduit.
 */
final class ServerSocketConduit private (

  private[this] final val serversocket: ServerSocket)

    extends SourceConduit[SocketConduit] {

  final def read: Future[SocketConduit] = {
    val promise = Promise[SocketConduit]
    object accepthandler extends Handler[Socket, Null] {
      @inline def failed(e: Throwable, a: Null) = {
        promise.tryFailure(e)
      }
      @inline def completed(socket: Socket, a: Null) = {
        promise.trySuccess(SocketConduit(socket))
      }
    }
    serversocket.accept(null: Null, accepthandler)
    promise.future
  }

}

/**
 *
 */
object ServerSocketConduit {

  def apply(address: InetSocketAddress, backlog: Int = 0): ServerSocketConduit = apply(
    ServerSocket
      .open(globalasynchronouschannelgroup)
      .setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.box(true))
      .setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(256 * 1024))
      .bind(address, backlog))

  def apply(serversocket: ServerSocket): ServerSocketConduit = new ServerSocketConduit(serversocket)

}

package aio
package conduit

import java.net.StandardSocketOptions.{ SO_KEEPALIVE, SO_RCVBUF, SO_REUSEADDR, SO_SNDBUF, TCP_NODELAY }
import java.nio.channels.{ AsynchronousSocketChannel ⇒ Socket }

import buffer.defaultCapacity

/**
 *
 */
final class SocketConduit private (

  protected[this] final val channel: Socket)

    extends ChannelConduit[Socket] {

  final override def toString = channel.toString

}

/**
 *
 */
object SocketConduit {

  def apply(channel: Socket) = {
    channel.setOption(TCP_NODELAY, Boolean.box(true))
    channel.setOption(SO_REUSEADDR, Boolean.box(true))
    channel.setOption(SO_KEEPALIVE, Boolean.box(false))
    channel.setOption(SO_RCVBUF, Integer.valueOf(defaultCapacity))
    channel.setOption(SO_SNDBUF, Integer.valueOf(defaultCapacity))
    new SocketConduit(channel)
  }

}

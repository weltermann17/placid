package aio
package conduit

import java.net.StandardSocketOptions.{ SO_KEEPALIVE, SO_RCVBUF, SO_REUSEADDR, SO_SNDBUF, TCP_NODELAY }
import java.nio.channels.{ AsynchronousSocketChannel ⇒ SocketChannel }

import buffer.defaultCapacity

/**
 *
 */
final class SocketChannelConduit private (

  protected[this] final val channel: SocketChannel)

    extends ChannelConduit[SocketChannel] {

  final override def toString = channel.toString

}

/**
 *
 */
object SocketChannelConduit {

  def apply(channel: SocketChannel) = {
    channel.setOption(TCP_NODELAY, Boolean.box(true))
    channel.setOption(SO_REUSEADDR, Boolean.box(true))
    channel.setOption(SO_KEEPALIVE, Boolean.box(false))
    channel.setOption(SO_RCVBUF, Integer.valueOf(defaultCapacity))
    channel.setOption(SO_SNDBUF, Integer.valueOf(defaultCapacity))
    new SocketChannelConduit(channel)
  }

}

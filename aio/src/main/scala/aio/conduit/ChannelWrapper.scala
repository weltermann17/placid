package aio
package conduit

import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousByteChannel ⇒ Channel, AsynchronousChannel ⇒ BaseChannel }
import java.util.concurrent.{ Future ⇒ JFuture }

import language.unsupported

/**
 *
 */
trait ChannelWrapper

    extends Channel {

  final def close = wrappedchannel.close

  final def isOpen = wrappedchannel.isOpen

  final def read(buffer: ByteBuffer): JFuture[Integer] = unsupported

  final def write(buffer: ByteBuffer): JFuture[Integer] = unsupported

  protected[this] val wrappedchannel: BaseChannel

}

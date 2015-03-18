package aio
package conduit

import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousByteChannel ⇒ Channel, AsynchronousChannel ⇒ BaseChannel }
import java.util.concurrent.{ Future ⇒ JFuture }

/**
 *
 */
trait ChannelWrapper

    extends Channel {

  final def close = wrappedchannel.close

  final def isOpen = wrappedchannel.isOpen

  final def read(buffer: ByteBuffer): JFuture[Integer] = ???

  final def write(buffer: ByteBuffer): JFuture[Integer] = ???

  protected[this] val wrappedchannel: BaseChannel

}

package aio
package conduit

import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousByteChannel ⇒ Channel, CompletionHandler ⇒ Handler }

import scala.concurrent.{ Future, Promise }

import buffer.{ RichByteBuffer, defaultCapacity }
import buffer.ByteBufferPool.{ acquire, release }

/**
 *
 */
trait ChannelConduit[C <: Channel]

  extends ChannelSourceConduit[C]

  with ChannelSinkConduit[C]

/**
 *
 */
trait ChannelSourceConduit[C <: Channel]

    extends ByteBufferSourceConduit {

  protected[this] val channel: C

  final def read: Future[ByteBuffer] = {
    val buffer = acquire(defaultCapacity)
    val promise = Promise[ByteBuffer]
    object readhandler extends Handler[Integer, Null] {
      @inline def failed(e: Throwable, a: Null) = {
        release(buffer)
        ignore(channel.close)
        promise.tryFailure(e)
      }
      @inline def completed(processed: Integer, a: Null) = {
        if (0 < processed.intValue) {
          buffer.flip
          promise.trySuccess(buffer)
        } else {
          release(buffer)
          ignore(channel.close)
          promise.tryFailure(ExpectedEOFException)
        }
      }
    }
    channel.read(buffer, null: Null, readhandler)
    promise.future
  }

}

/**
 *
 */
trait ChannelSinkConduit[C <: Channel]

    extends ByteBufferSinkConduit {

  protected[this] val channel: C

  final def write(buffer: ByteBuffer): Future[Unit] = {
    val promise = Promise[Unit]
    object writehandler extends Handler[Integer, Null] {
      @inline def failed(e: Throwable, a: Null) = {
        release(buffer)
        ignore(channel.close)
        promise.tryFailure(e)
      }
      @inline def completed(processed: Integer, a: Null) = {
        if (0 < buffer.remaining) {
          channel.write(buffer, null: Null, writehandler)
        } else {
          release(buffer)
          promise.trySuccess(())
        }
      }
    }
    channel.write(buffer, null: Null, writehandler)
    promise.future
  }

}

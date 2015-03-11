package aio
package conduit

import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousByteChannel, CompletionHandler }
import java.util.concurrent.Future

/**
 * A Conduit is an abstraction of an AsynchronousByteChannel. Note: Not of AsynchronousChannel. Classes directly derived from AsynchronousChannel need to be wrapped to become a Conduit.
 */
trait Conduit

  extends AsynchronousByteChannel

  with SourceConduit

  with SinkConduit

/**
 * Basic abstraction.
 */
trait BaseConduit {

  type Handler[A] = CompletionHandler[Integer, _ >: A]

  protected[this] abstract class BaseHandler[A](

    private[this] final val handler: Handler[A])

      extends CompletionHandler[Integer, A] {

    def failed(e: Throwable, attachment: A) = handler.failed(e, attachment)

  }

}

/**
 * A Conduit is split into its source for reading from it and its sink for writing to it. SourceConduit builds the base for read implementations.
 */
trait SourceConduit

    extends BaseConduit {

  /**
   * Asynchronous read.
   */
  def read[A](buffer: ByteBuffer, attachment: A, handler: Handler[A])

  /**
   * Not used.
   */
  def read(buffer: ByteBuffer): Future[Integer] = ???

}

/**
 * A Conduit is split into its source for reading from it and its sink for writing to it. SinkConduit builds the base for write implementations.
 */
trait SinkConduit

    extends BaseConduit {

  /**
   * Asynchronous write.
   */
  def write[A](buffer: ByteBuffer, attachment: A, handler: Handler[A])

  /**
   * Not used.
   */
  def write(buffer: ByteBuffer): Future[Integer] = ???

}

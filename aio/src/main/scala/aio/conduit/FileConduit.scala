package aio
package conduit

import java.io.{ File, RandomAccessFile }
import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousFileChannel ⇒ FileChannel, CompletionHandler ⇒ Handler }
import java.nio.file.{ Path, Paths }
import java.nio.file.Files.{ exists, size }
import java.nio.file.StandardOpenOption.{ APPEND, CREATE, READ, TRUNCATE_EXISTING, WRITE }

import scala.collection.JavaConversions.setAsJavaSet
import scala.language.implicitConversions

import concurrent.Implicits.globalexecutioncontext

/**
 *
 */
final class FileConduit private (

  protected[this] final val channel: FileChannelWrapper)

    extends ChannelSourceConduit[FileChannelWrapper]

    with ChannelSinkConduit[FileChannelWrapper]

/**
 * Convenience constructors
 */
object FileConduit {

  private[this] def apply(filechannel: FileChannel, position: Long): FileConduit = new FileConduit(new FileChannelWrapper(filechannel, position))

  def forReading(path: Path): FileConduit = apply(FileChannel.open(path, Set(READ), globalexecutioncontext), 0L)

  def forWriting(path: Path): FileConduit = apply(FileChannel.open(path, Set(CREATE, TRUNCATE_EXISTING, WRITE), globalexecutioncontext), 0L)

  def forAppending(path: Path): FileConduit = apply(FileChannel.open(path, Set(CREATE, WRITE), globalexecutioncontext), if (exists(path)) size(path) else 0L)

  /**
   * This is very fast and should, therefore, be preferred, it also fails if there is not enough space in the file system.
   */
  def forWriting(path: Path, length: Long): FileConduit = {
    val f = new RandomAccessFile(path.toString, "rw")
    f.setLength(length)
    f.close
    apply(FileChannel.open(path, Set(WRITE), globalexecutioncontext), 0L)
  }

  @inline implicit def file2path(file: File): Path = file.toPath

  @inline implicit def string2path(s: String): Path = Paths.get(s)

}

/**
 * Helper: Converts an AsynchronousFileChannel into an AsynchronousByteChannel.
 */
final class FileChannelWrapper private[conduit] (

  protected[this] final val wrappedchannel: FileChannel,

  private[this] final var position: Long)

    extends ChannelWrapper {

  final def read[A](buffer: ByteBuffer, attachment: A, handler: Handler[Integer, _ >: A]) = {
    wrappedchannel.read(buffer, position, attachment, new InnerHandler(handler))
  }

  final def write[A](buffer: ByteBuffer, attachment: A, handler: Handler[Integer, _ >: A]) = {
    wrappedchannel.write(buffer, position, attachment, new InnerHandler(handler))
  }

  private[this] final class InnerHandler[A](

    private[this] final val handler: Handler[Integer, A])

      extends Handler[Integer, A] {

    @inline final def failed(e: Throwable, attachment: A) = handler.failed(e, attachment)

    @inline final def completed(processed: Integer, attachment: A) = {
      if (0 < processed) position += processed
      handler.completed(processed, attachment)
    }

  }

}

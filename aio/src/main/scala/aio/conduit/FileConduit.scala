package aio
package conduit

import java.io.{ File, RandomAccessFile }
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.{ Path, Paths }
import java.nio.file.StandardOpenOption.{ CREATE, READ, TRUNCATE_EXISTING, WRITE }

import scala.collection.JavaConversions.setAsJavaSet

/**
 * Converts an AsynchronousFileChannel into a Conduit which is an AsynchronousByteChannel.
 */
final class FileConduit(

  private[this] final val filechannel: AsynchronousFileChannel)

    extends Conduit {

  final def read[A](buffer: ByteBuffer, attachment: A, handler: Handler[A]) = {
    filechannel.read(buffer, position, attachment, new FileHandler(handler))
  }

  final def write[A](buffer: ByteBuffer, attachment: A, handler: Handler[A]) = {
    filechannel.write(buffer, position, attachment, new FileHandler(handler))
  }

  final def close = filechannel.close

  final def isOpen = filechannel.isOpen

  private[this] final class FileHandler[A](

    private[this] final val handler: Handler[A])

      extends BaseHandler[A](handler) {

    final def completed(processed: Integer, attachment: A) = {
      if (0 < processed) position += processed
      handler.completed(processed, attachment)
    }

  }

  private[this] final var position = 0L

}

/**
 * Helpers for FileConduit creation.
 */
object FileConduit {

  final def forReading(path: Path): FileConduit = apply(AsynchronousFileChannel.open(path, Set(READ), null))

  final def forWriting(path: Path): FileConduit = apply(AsynchronousFileChannel.open(path, Set(CREATE, TRUNCATE_EXISTING, WRITE), null))

  final def forReading(file: File): FileConduit = forReading(file.toPath)

  final def forReading(path: String): FileConduit = forReading(Paths.get(path))

  final def forWriting(file: File): FileConduit = forWriting(file.toPath)

  final def forWriting(path: String): FileConduit = forWriting(Paths.get(path))

  final def forWriting(file: File, length: Long): FileConduit = forWriting(file.toPath, length)

  final def forWriting(path: String, length: Long): FileConduit = forWriting(Paths.get(path), length)

  /**
   * This is very fast and should, therefore, be preferred, it also fails if there is not enough space in the file system.
   */
  final def forWriting(path: Path, length: Long): FileConduit = {
    val f = new RandomAccessFile(path.toString, "rw")
    f.setLength(length)
    f.close
    apply(AsynchronousFileChannel.open(path, Set(WRITE), null))
  }

  private[this] final def apply(filechannel: AsynchronousFileChannel) = new FileConduit(filechannel)

}

package aio

import java.io.EOFException
import java.nio.ByteBuffer

/**
 *
 * Some convenient helpers for asynchronous io.
 *
 */
package object buffer {

  /**
   * Implicit helper for ByteBuffer.
   */
  final implicit class RichByteBuffer(

    private[this] final val buffer: ByteBuffer)

      extends PooledByteBuffer(buffer)

  /**
   *
   */
  final val defaultCapacity = 64 * 1024

  /**
   *
   */
  object ExpectedEOFException extends EOFException

}

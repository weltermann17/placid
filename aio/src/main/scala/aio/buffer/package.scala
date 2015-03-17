package aio

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

      extends DumpByteBuffer(buffer)

  /**
   *
   */
  final val defaultCapacity = 64 * 1024

}

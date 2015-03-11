import java.nio.ByteBuffer

/**
 *
 * Some convenient helpers for asynchronous io.
 *
 */
package object aio {

  /**
   * Implicit helper for ByteBuffer.
   */
  final implicit class RichByteBuffer(

    protected[this] final val buffer: ByteBuffer)

      extends BaseByteBuffer

}

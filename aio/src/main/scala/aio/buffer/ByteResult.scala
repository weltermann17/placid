package aio
package buffer

import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

import scala.language.implicitConversions

/**
 *
 */
final class ByteResult private (

    private[this] final val buffer: ByteBuffer,

    final val isLast: Boolean) {

  final def release: Unit = if (isreleased.compareAndSet(false, true)) ByteBufferPool.release(buffer)

  override final def toString = s"ByteResult(buffer = $buffer, isLast = $isLast, isReleased = $isreleased)"

  @inline private final def asByteBuffer = buffer

  private[this] final val isreleased = new AtomicBoolean(false)

}

/**
 *
 */
object ByteResult {

  def default: ByteResult = apply(defaultCapacity, false)

  val sentinel: ByteResult = apply(ByteBuffer.allocateDirect(0), true)

  def apply(islast: Boolean): ByteResult = apply(defaultCapacity, islast)

  def apply(capacity: Int, islast: Boolean): ByteResult = apply(ByteBufferPool.acquire(capacity), islast)

  def apply(buffer: ByteBuffer, islast: Boolean): ByteResult = new ByteResult(buffer, islast)

  @inline implicit private[aio] def asByteBuffer(byteresult: ByteResult): ByteBuffer = byteresult.asByteBuffer

}

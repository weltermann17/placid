package aio
package buffer

import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

import scala.annotation.tailrec

/**
 *
 */
private class ByteBufferPool private (

    private[this] final val capacity: Int,

    private[this] final val poolsize: Int,

    private[this] final val direct: Boolean) {

  @tailrec final def acquire: ByteBuffer = {
    if (trylock) {
      try pool match {
        case head +: tail ⇒
          pool = tail
          head
        case _ ⇒
          newBuffer
      } finally unlock
    } else {
      acquire
    }
  }

  @tailrec final def release(buffer: ByteBuffer): Unit = {
    if (trylock) try {
      buffer.clear
      pool = buffer +: pool
    } finally unlock
    else {
      release(buffer)
    }
  }

  /**
   * Maybe expensive.
   */
  final def size = pool.size

  @inline private[this] final def newBuffer: ByteBuffer = if (direct) ByteBuffer.allocateDirect(capacity) else ByteBuffer.allocate(capacity)

  @inline private[this] final def trylock = locked.compareAndSet(false, true)

  @inline private[this] final def unlock = locked.set(false)

  private[this] final val locked = new AtomicBoolean(false)

  private[this] final var pool: Vector[ByteBuffer] = (1 to poolsize).toVector.map(_ ⇒ newBuffer)

}

/**
 *
 */
object ByteBufferPool {

  def acquire(capacity: Int): ByteBuffer = pools.get(capacity) match {
    case Some(pool) ⇒ pool.acquire
    case _ ⇒ ByteBuffer.allocate(capacity)
  }

  private[buffer] def release(buffer: ByteBuffer): Unit = pools.get(buffer.capacity) match {
    case Some(pool) ⇒ pool.release(buffer)
    case _ ⇒
  }

  def create(capacity: Int, poolsize: Int, direct: Boolean): Unit = pools = pools ++ Map(capacity -> new ByteBufferPool(capacity, poolsize, direct))

  private[this] final var pools: Map[Int, ByteBufferPool] = Map.empty

}

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
          head.clear
          head
        case _ ⇒
          newbuffer
      } finally unlock
    } else {
      acquire
    }
  }

  @tailrec final def release(buffer: ByteBuffer): Unit = {
    if (trylock) try {
      pool = buffer +: pool
    } finally unlock
    else {
      release(buffer)
    }
  }

  /**
   * Potentially expensive method to check if a ByteBuffer has already been released back into the pool.
   */
  @tailrec final def find(buffer: ByteBuffer): Boolean = {
    if (trylock) try {
      pool.exists(_ eq buffer)
    } finally unlock
    else {
      find(buffer)
    }
  }

  /**
   * Maybe expensive.
   */
  final def size = pool.size

  @inline private[this] final def newbuffer: ByteBuffer = if (direct) ByteBuffer.allocateDirect(capacity) else ByteBuffer.allocate(capacity)

  @inline private[this] final def trylock = locked.compareAndSet(false, true)

  @inline private[this] final def unlock = locked.set(false)

  private[this] final val locked = new AtomicBoolean(false)

  private[this] final var pool: List[ByteBuffer] = (1 to poolsize).toList.map(_ ⇒ newbuffer)

}

/**
 *
 */
object ByteBufferPool {

  /**
   *  Will return a cleared ByteBuffer of the given capacity.
   */
  def acquire(capacity: Int): ByteBuffer = pools.get(capacity) match {
    case Some(pool) ⇒ pool.acquire
    case _ ⇒ ByteBuffer.allocate(capacity)
  }

  def release(buffer: ByteBuffer): Unit = pools.get(buffer.capacity) match {
    case Some(pool) ⇒ pool.release(buffer)
    case _ ⇒
  }

  def isReleased(buffer: ByteBuffer): Boolean = pools.get(buffer.capacity) match {
    case Some(pool) ⇒ pool.find(buffer)
    case _ ⇒ false
  }

  def create(capacity: Int, poolsize: Int, direct: Boolean): Unit = pools = pools ++ Map(capacity -> new ByteBufferPool(capacity, poolsize, direct))

  private[this] final var pools: Map[Int, ByteBufferPool] = Map.empty

}

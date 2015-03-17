package aio
package conduit

import java.nio.ByteBuffer

import scala.concurrent.Future

import buffer.defaultCapacity

/**
 *
 */
trait Conduit

  extends SourceConduit

  with SinkConduit

/**
 *
 */
trait SourceConduit {

  def read(capacity: Int): Future[ByteBuffer]

}

/**
 *
 */
trait SinkConduit {

  def write(buffer: ByteBuffer): Future[Unit]

}

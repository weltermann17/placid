package aio
package conduit

import scala.concurrent.Future

/**
 *
 */
trait Conduit[A]

  extends SourceConduit[A]

  with SinkConduit[A]

/**
 *
 */
trait SourceConduit[A] {

  def read: Future[A]

}

/**
 *
 */
trait SinkConduit[A] {

  def write(a: A): Future[Unit]

}

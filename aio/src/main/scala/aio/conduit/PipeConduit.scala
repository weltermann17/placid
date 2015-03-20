package aio
package conduit

import scala.concurrent.Future

/**
 *
 */
trait PipeConduit[A]

  extends SourceConduit[A]

  with SinkConduit[A]

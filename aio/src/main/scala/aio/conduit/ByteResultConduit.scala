package aio
package conduit

import buffer.ByteResult

/**
 *
 */
trait ByteResultSourceConduit

  extends SourceConduit[ByteResult]

/**
 *
 */
trait ByteResultSinkConduit

  extends SinkConduit[ByteResult]

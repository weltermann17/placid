package aio
package conduit

import buffer.ByteResult

/**
 *
 */
trait ByteResultConduit

  extends ByteResultSourceConduit

  with ByteResultSinkConduit

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

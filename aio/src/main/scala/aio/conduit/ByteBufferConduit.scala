package aio
package conduit

import java.nio.ByteBuffer

/**
 *
 */
trait ByteBufferConduit

  extends ByteBufferSourceConduit

  with ByteBufferSinkConduit

/**
 *
 */
trait ByteBufferSourceConduit

  extends SourceConduit[ByteBuffer]

/**
 *
 */
trait ByteBufferSinkConduit

  extends SinkConduit[ByteBuffer]

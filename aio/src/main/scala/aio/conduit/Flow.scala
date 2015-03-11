package aio
package conduit

import java.nio.ByteBuffer
import java.nio.channels.{ AsynchronousByteChannel, CompletionHandler }

/**
 *
 */
final class Flow private (
    private[this] final val source: Conduit,
    private[this] final val sink: Conduit,
    private[this] final val buffer: ByteBuffer,
    private[this] final val outerhandler: CompletionHandler[Long, Any]) {

  def flow = read

  private def available = buffer.remaining

  private[this] def read = {
    buffer.clear
    source.read(buffer, this, ReadHandler)
  }

  private[this] def write(handler: FlowHandler, flip: Boolean) = {
    if (flip) buffer.flip
    println(s"write ${buffer.dump}")
    sink.write(buffer, this, handler)
  }

  private[this] def close = {
    source.close
    sink.close
    println("close")
    outerhandler.completed(total, this)
  }

  private[this] sealed abstract class FlowHandler extends CompletionHandler[Integer, Flow] {

    def failed(e: Throwable, flow: Flow) = outerhandler.failed(e, this)

  }

  private[this] object ReadHandler extends FlowHandler {

    @inline def completed(processed: Integer, flow: Flow) = {
      if (0 < processed) total += processed
      if (0 < processed) write(WriteHandler, true) else write(CloseHandler, true)
    }

  }

  private[this] object WriteHandler extends FlowHandler {

    @inline def completed(processed: Integer, flow: Flow) = {
      if (0 < flow.available) write(this, false) else read
    }

  }

  private[this] object CloseHandler extends FlowHandler {

    @inline def completed(processed: Integer, flow: Flow) = {
      if (0 < flow.available) write(this, false) else close
    }

  }

  private[this] final var total = 0L

}

/**
 *
 */
object Flow {

  def apply(source: Conduit, sink: Conduit, outerhandler: CompletionHandler[Long, Any]) = {
    new Flow(source, sink, newBuffer, outerhandler)
  }

  def newBuffer = ByteBuffer.allocateDirect(16 * 1024)

}

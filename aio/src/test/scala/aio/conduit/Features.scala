package aio
package conduit

import org.scalatest.{ FeatureSpec, Matchers }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import java.nio.channels.{ AsynchronousByteChannel, CompletionHandler }

/**
 * Testing all Conduit features.
 */
class Features

    extends FeatureSpec

    with Matchers

    with GeneratorDrivenPropertyChecks {

  feature("Conduit") {
    scenario("Copy from one file to another") {
      val source = FileConduit.forReading("/tmp/test.source")
      val sink = FileConduit.forWriting("/tmp/test.sink")
      val pipe = new PipeConduit
      val barrier = new java.util.concurrent.CyclicBarrier(2)
      val handler = new CompletionHandler[Long, Any] {
        def failed(e: Throwable, any: Any) = { println(e); barrier.await }
        def completed(processed: Long, any: Any) = { println(s"processed : $processed"); barrier.await }
      }
      val flow1 = Flow(source, pipe, handler)
      val flow2 = Flow(pipe, sink, handler)
      flow1.flow
      flow2.flow
      println("after flow")
      barrier.await
      println("after await")
    }
  }

}

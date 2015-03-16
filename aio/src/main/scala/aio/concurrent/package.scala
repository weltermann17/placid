package aio

import java.nio.channels.AsynchronousChannelGroup
import java.util.Collections
import java.util.concurrent.{ AbstractExecutorService, Executors, TimeUnit }

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContextExecutorService

/**
 *
 */
package object concurrent {

  object ExecutionContextExecutorServiceBridge {

    final def apply(context: ExecutionContext): ExecutionContextExecutorService = context match {
      case null ⇒ throw null
      case context: ExecutionContextExecutorService ⇒ context
      case other ⇒ new AbstractExecutorService with ExecutionContextExecutorService {
        @inline override def prepare: ExecutionContext = other
        @inline override def isShutdown = false
        @inline override def isTerminated = false
        @inline override def shutdown = ()
        @inline override def shutdownNow = Collections.emptyList[Runnable]
        @inline override def execute(runnable: Runnable): Unit = other execute runnable
        @inline override def reportFailure(t: Throwable): Unit = other reportFailure t
        @inline override def awaitTermination(length: Long, unit: TimeUnit): Boolean = false
      }
    }

  }

  object Implicits {

    implicit final val globalexecutioncontext = ExecutionContextExecutorServiceBridge(global)

    implicit final val globalscheduledexecutor = Executors.newScheduledThreadPool(sys.runtime.availableProcessors)

    implicit final val globalasynchronouschannelgroup = AsynchronousChannelGroup.withThreadPool(globalexecutioncontext)

  }
}

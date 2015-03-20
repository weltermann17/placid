package aio
package concurrent

import java.util.concurrent.{ ScheduledExecutorService, ScheduledFuture, TimeUnit }

import scala.annotation.tailrec
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.concurrent.duration.{ Duration, DurationInt }
import scala.util.Try
import scala.util.control.NonFatal

/**
 *
 */
object FutureAddons {

  /**
   * "Select" off the first future to be satisfied. @Returns the first as a Try, with the remainder of the Futures as a sequence of Futures.
   */
  def select[A](fs: Seq[Future[A]])(implicit e: ExecutionContext): Future[(Try[A], Seq[Future[A]])] = {
    @tailrec @inline def stripe(
      p: Promise[(Try[A], Seq[Future[A]])],
      headseq: Seq[Future[A]],
      elem: Future[A],
      tail: Seq[Future[A]]): Future[(Try[A], Seq[Future[A]])] = {
      elem onComplete { res ⇒ if (!p.isCompleted) p.trySuccess((res, headseq ++ tail)) }
      tail match {
        case head +: tail ⇒ stripe(p, headseq :+ elem, head, tail)
        case _ ⇒ p.future
      }
    }
    fs match {
      case head +: tail ⇒ stripe(Promise(), fs.genericBuilder[Future[A]].result, head, tail)
      case _ ⇒ Future.failed(new IllegalArgumentException("Cannot call select with an empty Seq."))
    }
  }

  /**
   * Schedule @param body to be executed only once after @param duration. @Returns a ScheduleFuture eg. for cancelling.
   */
  def scheduleOnce(duration: Duration)(body: ⇒ Any)(implicit s: ScheduledExecutorService): ScheduledFuture[_] = {
    s.schedule(new Runnable { def run = body }, duration.toMillis, TimeUnit.MILLISECONDS)
  }

  /**
   * @Returns a Future that will be completed with the success or failure of the provided value after the specified duration.
   */
  def after[A](duration: Duration)(value: ⇒ Future[A])(implicit s: ScheduledExecutorService): Future[A] =
    if (duration.isFinite() && duration.length < 1) {
      try value catch { case NonFatal(t) ⇒ Promise.failed(t).future }
    } else {
      val p = Promise[A]()
      scheduleOnce(duration) { p completeWith { try value catch { case NonFatal(t) ⇒ Promise.failed(t).future } } }
      p.future
    }

  /**
   * @Returns either the value or the timeout Future which ever comes first.
   */
  def timeoutAfter[A](duration: Duration)(timeout: ⇒ Future[A], value: ⇒ Future[A])(implicit e: ExecutionContext, s: ScheduledExecutorService): Future[A] = {
    Future.firstCompletedOf(Seq(after(duration)(timeout), value))
  }

}

package aio

import scala.concurrent.Future
import concurrent.Implicits.globalexecutioncontext

/**
 */
package object conduit {

  /**
   * Swallow any exception that might get thrown in @param p and ignore it.
   */
  @inline final def ignore(p: ⇒ Any) = try p catch { case _: Throwable ⇒ }

  /**
   * Very clever monadic helper for nested for comprehensions to loop at a certain level.
   */
  final def loop(m: ⇒ Future[_]): Future[_] = for {
    _ ← m
    _ ← loop(m)
  } yield ()

  /**
   *
   */
  final def loop(m: ⇒ Future[_], n: ⇒ Future[_]): Future[_] = for {
    _ ← m
    _ ← { n; loop(m, n) }
  } yield ()

}

package aio

import scala.concurrent.Future
import concurrent.Implicits.globalexecutioncontext

/**
 */
package object conduit {

  /**
   * Very clever monadic helper for nested for comprehensions to loop at a certain level.
   */
  @inline final def loop(m: ⇒ Future[_]): Future[_] = for {
    _ ← m
    _ ← loop(m)
  } yield ()

}

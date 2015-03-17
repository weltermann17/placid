package aio

import java.io.EOFException

import scala.util.control.NoStackTrace

/**
 *
 * Some convenient helpers for asynchronous io.
 *
 */
package object conduit {

  /**
   * Swallow any exception that might get thrown in @param p and ignore it.
   */
  @inline final def ignore(p: ⇒ Any) = try p catch { case _: Throwable ⇒ }

  /**
   * Thrown when a peer closes a socket connection.
   */
  object ExpectedEOFException

      extends EOFException

      with NoStackTrace {

    override final def toString = "ExpectedEOFException"

  }

}

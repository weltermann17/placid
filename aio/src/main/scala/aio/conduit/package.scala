package aio

import java.io.EOFException
import java.nio.ByteBuffer

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

}

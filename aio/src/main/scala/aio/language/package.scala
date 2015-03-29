package aio

import java.lang.management.ManagementFactory.getThreadMXBean

/**
 * Helpers to keep the code concise.
 */
package object language {

  /**
   * Swallow any exception that might get thrown in @param p and ignore it.
   */
  @inline def ignore(p: ⇒ Any) = try p catch { case _: Throwable ⇒ }

  /**
   * To check for potential stack overflow issues.
   */
  def stackDepth: Int = {
    val mxBean = getThreadMXBean
    val threadInfo = mxBean.getThreadInfo(Thread.currentThread.getId, Int.MaxValue)
    threadInfo.getStackTrace.length
  }

  /**
   * Throws a hint that the caller tries to access something unsupported.
   */
  def unsupported = {
    @inline def caller(depth: Int): String = {
      val mxBean = getThreadMXBean
      val threadInfo = mxBean.getThreadInfo(Thread.currentThread.getId, depth)
      val elements = threadInfo.getStackTrace
      elements(depth - 1).getClassName + "." + elements(depth - 1).getMethodName
    }
    throw new UnsupportedOperationException(caller(6))
  }

  /**
   * Handy, as it is used very often.
   */
  def illegalState(message: ⇒ String) = throw new IllegalStateException(message)

}

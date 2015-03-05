package placid
package util

import java.io.{ PrintWriter, StringWriter }
import java.nio.charset.StandardCharsets.{ US_ASCII, UTF_8 }

import placid.util.text.HexifyString

/**
 *
 * Some convenient helpers for string handling.
 *
 */
package object text {

  /**
   * Implicit helper for HexifyString and more.
   */
  final implicit class RichString(s: String)

    extends HexifyString(s)

  /**
   * Add convenient helpers to Exceptions.
   */
  final implicit class RichThrowable(e: Throwable) {

    /**
     * Convert the stack trace to a string.
     */
    final def stackAsString: String = {
      val s = new StringWriter(4096)
      e.printStackTrace(new PrintWriter(s))
      s.toString
    }

  }

}

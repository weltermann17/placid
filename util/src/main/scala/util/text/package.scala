package util

import java.io.{ PrintWriter, StringWriter }
import java.nio.charset.StandardCharsets.{ US_ASCII, UTF_8 }

/**
 *
 * Some convenient helpers for string handling.
 *
 */
package object text {

  /**
   * Implicit helper for HexifyString and more.
   */
  final implicit class RichString(

    protected[this] final val s: String)

      extends HexifyString

}

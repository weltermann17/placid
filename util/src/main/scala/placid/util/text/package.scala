package placid
package util

import java.nio.charset.StandardCharsets.{ US_ASCII, UTF_8 }

import placid.util.text.HexifyString

/**
 *
 * Some convenient helpers for string handling.
 *
 */
package object text {

  /**
   * Just aliases for the most important charsets.
   */
  val utf8 = UTF_8

  val `UTF-8` = utf8

  val ascii = US_ASCII

  val `US -ASCII` = ascii

  /**
   * Implicit helper for HexifyString and more.
   */
  final implicit class RichString(s: String)

    extends HexifyString(s)

}

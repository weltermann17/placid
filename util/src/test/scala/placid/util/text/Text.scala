package placid
package util
package text

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.{ ISO_8859_1, UTF_16 }

import org.scalatest.{ Finders, FlatSpec, Matchers }

class Text

    extends FlatSpec

    with Matchers {

  "A HexifyString" should "fulfill simple assertions" in {
    assert("This is a simple string.")
    assert(new String("abcABCöäüÖÄÜß".getBytes(`UTF-8`), ISO_8859_1), ISO_8859_1)
    assert("華語中文华语漢語汉语")
    assert(new String("華語中文华语漢語汉语".getBytes(`UTF-8`), UTF_16), UTF_16)
  }

  def assert(s: String, charset: Charset = `UTF-8`): Unit = {
    val h = s.hexify(charset)
    assert(2 * s.getBytes(charset).length == h.length)
    assert(s == h.unhexify(charset))
    assert(s == h.toLowerCase.unhexify(charset))
  }

  val o = placid.util.text.`package` // avoid load problem with package object

}

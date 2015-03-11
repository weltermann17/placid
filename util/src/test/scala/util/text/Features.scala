package util
package text

import java.nio.charset.Charset.defaultCharset
import java.nio.charset.StandardCharsets.{ ISO_8859_1, UTF_16, UTF_8 }

import org.scalacheck.Arbitrary.{ arbString, arbitrary }
import org.scalacheck.Gen
import org.scalatest.{ FeatureSpec, Matchers }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

/**
 * Testing all text features.
 */
class Features

    extends FeatureSpec

    with Matchers

    with GeneratorDrivenPropertyChecks {

  implicit override val generatorDrivenConfig = PropertyCheckConfig(minSuccessful = 500, maxDiscarded = 50, workers = 8)

  feature("HexifyString") {
    scenario("A hexified and then unhexified string is equal to the original string") {
      info(s"Using default characterset : $defaultCharset")
      forAll(input) {
        case (s, c) ⇒
          Seq(s.hexify(c) -> s.hexify(c).unhexify(c),
            s.hexifyCrypted(c) -> s.hexifyCrypted(c).unhexifyCrypted(c)) map {
              case (h, u) ⇒
                u shouldEqual s
                h should have length 2 * s.getBytes(c).length
            }
      }
    }

    /**
     * It will not work for US_ASCII.
     */
    lazy val input = for {
      s ← arbitrary[String]
      c ← Gen.oneOf(UTF_8, UTF_16, ISO_8859_1)
    } yield (new String(s.getBytes(defaultCharset), c), c)

  }

}

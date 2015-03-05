package placid.util

import org.scalatest._

class ExampleSpec

    extends FlatSpec

    with Matchers {

  "s" should "be equal to 'Hello'" in {
    s should have length 5
    s should have size 5
    s should startWith("Hello")
  }

  "d" should "throw RuntimeException" in {
    a[RuntimeException] should be thrownBy {
      d
    }
  }
}

package aio
package conduit

/**
 *
 * exchange szenarios
 *
 * alternatives:
 *
 * a. read bytebuffer contains full http request and nothing more, http response can be written in one bytebuffer
 * b. read bytebuffer contains full http request and nothing more, http response must be written with many bytebuffers
 * c.
 *
 *
 *
 */

import scala.util.Random.nextBytes
import java.util.Calendar
import java.nio.charset.StandardCharsets.US_ASCII

object p extends App {

  val m = 1
  val n = 1000000
  val o = 10

  val index = 100
  val remainder = 1000

  private[this] final val pattern = """\r\n\r\n""".getBytes

  private[this] final val data = {
    val a = new Array[Byte](index)
    val b = new Array[Byte](remainder)
    nextBytes(a)
    nextBytes(b)
    a ++ pattern ++ b
  }

  def now = Calendar.getInstance.getTimeInMillis

  private[this] final val failure: Array[Int] = {
    val failure = new Array[Int](pattern.length)
    var j = 0
    var i = 1
    while (i < pattern.length) {
      while (j > 0 && pattern(j) != pattern(i)) j = failure(j - 1)
      if (pattern(j) == pattern(i)) j += 1
      failure(i) = j
      i += 1
    }
    failure
  }

  private[this] final def indexOf: Int = {
    var j = 0
    var i = 0
    while (i < data.length) {
      while (j > 0 && pattern(j) != data(i)) j = failure(j - 1)
      if (pattern(j) == data(i)) j += 1
      if (j == pattern.length) return i - pattern.length + 1
      i += 1
    }
    -1
  }

  def test1 = index == data.indexOfSlice(pattern)

  def test2 = index == indexOf

  def test3 = {
    val ss = new String(pattern, US_ASCII)
    val datas = new String(data, US_ASCII)
    index == datas.indexOf(ss)
  }

  for (_ ← 1 to o) {

    for (_ ← 1 to m) {
      val b = now
      for (i ← 1 to n) assert(test1)
      val e = now
      println(s"1 ${e - b}, ${(n / ((e - b) / 1000.0)).toInt}")
    }

    for (_ ← 1 to m) {
      val b = now
      for (i ← 1 to n) assert(test2)
      val e = now
      println(s"2 ${e - b}, ${(n / ((e - b) / 1000.0)).toInt}")
    }

    for (_ ← 1 to m) {
      val b = now
      for (i ← 1 to n) assert(test3)
      val e = now
      println(s"3 ${e - b}, ${(n / ((e - b) / 1000.0)).toInt}")
    }

  }

}

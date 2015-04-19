package http

import scala.util.control.NonFatal
import scala.language.implicitConversions

trait Request

trait CompleteRequest extends Request

trait IncompleteRequest extends Request

abstract sealed trait Try[+A] {

  def get: A

}

object Try {

  def apply[A](b: ⇒ A): Try[A] = try Success(b) catch { case NonFatal(e) ⇒ Failure(e) }

  def test2[A](t: scala.util.Try[A]): A = t match {
    case scala.util.Success(a) ⇒ a
    case scala.util.Failure(e) ⇒ throw e
  }

  def test3[A](t: Try[A]): A = t match {
    case Success(a) ⇒ a
    case Failure(e) ⇒ throw e
  }

  def play: Try[String] = Success("hello")

  def play2: scala.util.Try[String] = scala.util.Success("hello")

}

final case class Success[+A](

  final val value: A)

    extends Try[A] {

  @inline def get: A = value

}

final case class Failure[+A](

  final val exception: Throwable)

    extends Try[A] {

  @inline def get: A = throw exception

}

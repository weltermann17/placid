package aio
package conduit

import java.nio.ByteBuffer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

import scodec.bits._
import scodec.codecs._

/**
 *
 */
class PipeConduit

    extends Conduit {

  final def isOpen = true

  final def close = ()

  final def read[A](buffer: ByteBuffer, attachment: A, handler: Handler[A]) = {
    val f = Future[Integer] { println; 1 }
    f onComplete {
      case Success(processed) ⇒
        val otp = hex"54686973206973206e6f74206120676f6f642070616420746f2075736521".bits
        val bits = hex"746be39ece241e0da28b7acd4fad63632249ec5e2e402d5a0b2cd95d0a05".bits
        val decoded = (bits ^ otp) rotateLeft 3
        println(decoded)
        val msg = variableSizeBytes(uint16, utf8).decode(decoded).require.value
        println(msg)
        println("success")
        handler.completed(processed, attachment)
      case Failure(e) ⇒ println("failure"); handler.failed(e, attachment)
    }
  }

  final def write[A](buffer: ByteBuffer, attachment: A, handler: Handler[A]) = {
    println(s"pipe.write ${buffer.dump}")
  }

  private def newBuffer = ByteBuffer.allocateDirect(16 * 1024)

}

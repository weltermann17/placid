package aio
package conduit

import java.io.EOFException

import scala.util.control.NoStackTrace

/**
 *
 */
object Control {

  /**
   * Thrown when a SourceConduit completes a read with processed <= 0.
   */
  object EOF

      extends EOFException

      with NoStackTrace {

    override final def toString = "Control.EOF"

  }

}

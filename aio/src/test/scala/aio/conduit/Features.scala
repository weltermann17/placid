package aio
package conduit

import org.scalatest.{ FeatureSpec, Matchers }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import java.nio.channels.{ AsynchronousByteChannel, CompletionHandler }

/**
 * Testing all Conduit features.
 */
class Features

    extends FeatureSpec

    with Matchers

    with GeneratorDrivenPropertyChecks {

}

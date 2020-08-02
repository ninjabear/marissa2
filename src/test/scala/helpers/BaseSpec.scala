package helpers

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inspectors
import org.scalatest.wordspec.AnyWordSpec

class BaseSpec extends AnyWordSpec with MockFactory with Matchers with Inspectors

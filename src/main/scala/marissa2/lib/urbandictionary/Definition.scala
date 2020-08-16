package marissa2.lib.urbandictionary
import argonaut._, Argonaut._

case class Definition(description: String)

object Definition {
  implicit def DefinitionCodecJson: CodecJson[Definition] =
    casecodec1(Definition.apply, Definition.unapply)("definition")
}
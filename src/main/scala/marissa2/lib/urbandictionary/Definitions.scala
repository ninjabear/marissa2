package marissa2.lib.urbandictionary

import argonaut.Argonaut.casecodec1
import argonaut.CodecJson

case class Definitions(list: List[Definition]) {}

object Definitions {
    implicit def DefinitionsCodecJson: CodecJson[Definitions] =
      casecodec1(Definitions.apply, Definitions.unapply)("list")
}

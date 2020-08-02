package marissa2

import marissa2.models.Message

import scala.util.matching.Regex

object NameFilter {

  val name: Regex = """(?i)^marissa""".r

  def apply(message: (Message => Unit)): (Message) => Unit = (m) => {
    name.findFirstMatchIn(m.message) match {
      case Some(name) => message(m.copy(message = m.message.replaceFirst(name+" ","").replaceFirst("\\s+","")))
      case None => ()
    }
  }
}

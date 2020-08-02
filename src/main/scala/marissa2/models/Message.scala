package marissa2.models

case class Message(from: Option[String],
                   contents: String,
                   reply: (String) => Unit)

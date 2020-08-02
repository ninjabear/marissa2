package marissa2.models

case class Message(from: Option[String],
                   message: String,
                   reply: (String) => Unit)

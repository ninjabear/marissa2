package marissa2

case class Message(from: Option[String],
                   message: String,
                   reply: (String) => Unit)

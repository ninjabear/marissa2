package marissa2.lib

import marissa2.models.Message

object Greetings {

  def greet(m: Message): Unit = {
    if (m.contents.startsWith("hello"))
      m.reply("Hello")
  }

}

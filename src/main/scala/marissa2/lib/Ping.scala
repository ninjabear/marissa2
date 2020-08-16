package marissa2.lib

import marissa2.models.Message

object Ping {

  def ping(m: Message): Unit = {
    if (m.contents.startsWith("ping"))
      m.reply("pong")
  }

}

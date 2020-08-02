package lib

import marissa2.Message

object Ping {

  def ping(m: Message): Unit = {
    if (m.message.startsWith("ping"))
      m.reply("pong")
  }

}

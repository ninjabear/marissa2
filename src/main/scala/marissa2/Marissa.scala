package marissa2

import com.typesafe.scalalogging.Logger
import discord4j.core.DiscordClient
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent

class Marissa(val token: String, logger: Logger, brains: (Message) => Unit) {

  def awake(): Unit = {
    val client: DiscordClient = DiscordClient.create(token)

    client
      .getEventDispatcher
      .on(classOf[ReadyEvent])
      .subscribe((ready: ReadyEvent) => {
        logger.info(s"logged in as ${ready.getSelf.getUsername} (${ready.getSelf.getId}")
      })

    client
      .getEventDispatcher
      .on(classOf[MessageCreateEvent])
      .subscribe((m: MessageCreateEvent) => {
        logger.debug(m.toString)
        if (m.getMessage.getContent.isPresent) {
          val message = m.getMessage.getContent.get()
          brains(
            Message(
              if (m.getMember.isPresent) Some(m.getMember.get().getDisplayName) else None,
              message,
              (s: String) => {
                m.getMessage
                  .getChannel
                  .block()
                  .createMessage(s)
                  .block()
              }
            )
          )
        }
      }
      )

    logger.debug("logging in")
    client.login().block()
    logger.debug("logged out")
  }

}

object Marissa {

  def apply(token: String)(implicit logger: Logger): Unit = {
    new Marissa(token, logger, (m) => logger.debug(m.toString)).awake()
  }

}

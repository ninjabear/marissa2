package marissa2

import com.typesafe.scalalogging.Logger
import discord4j.core.DiscordClient
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import marissa2.models.Message

import scala.concurrent.Future

object Connector {

  import scala.concurrent.ExecutionContext.Implicits._

  def connectAndBlock(token: String,
                      dispatcher: (Message) => Future[Unit]
                     )(implicit logger: Logger): Unit = {

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

        val validMessage = m.getMessage.getContent.isPresent &&
          m.getMessage.getAuthor.isPresent &&
          !m.getMessage.getAuthor.get().isBot

        if (validMessage) {
          val message = m.getMessage.getContent.get()
            dispatcher(
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
            ) recover {
              case e => logger.error("dispatch failed", e)
            }
        }
      }
      )

    logger.debug("logging in")
    client.login().block()
    logger.debug("logged out")
  }

}

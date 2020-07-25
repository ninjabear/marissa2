package marissa2

import com.typesafe.scalalogging.Logger

object Main extends App {
  implicit val logger: Logger = Logger("marissa2")

  val token = sys.env("DISCORD_BOT_TOKEN")
  if (token.isEmpty()) {
    logger.warn("DISCORD_BOT_TOKEN is empty!")
  }

  logger.info("marrisa2 awake")
  Marissa(token)
  logger.info("marissa2 lights off")
}
package marissa2

import com.typesafe.scalalogging.Logger
import lib.Selfie

object Main extends App {
  implicit val logger: Logger = Logger("marissa2")

  val token = sys.env("DISCORD_BOT_TOKEN")
  if (token.isEmpty) {
    logger.warn("DISCORD_BOT_TOKEN is empty!")
  }

  val version = sys.env.get("BUILD_COMMIT")

  logger.info("marrisa2 awake")

  Connector.connectAndBlock(
    token,
    Dispatcher(
      Seq(
        Selfie.selfie
      )
    ).dispatch
  )

  logger.info("marissa2 lights off")
}
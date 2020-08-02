package marissa2

import com.typesafe.scalalogging.Logger
import lib.{Ping, Selfie}

object Main extends App {
  implicit val logger: Logger = Logger("marissa2")

  val token = sys.env("DISCORD_BOT_TOKEN")
  if (token.isEmpty) {
    logger.warn("DISCORD_BOT_TOKEN is empty!")
  }

  val version = sys.env.get("BUILD_COMMIT")

  logger.info(s"marrisa2 awake [$version]")

  Connector.connectAndBlock(
    token,
    Dispatcher(
      Seq(
        (m:Message) => Selfie.selfie(m),
        Ping.ping
      )
    ).dispatch
  )

  logger.info("marissa2 lights off")
}
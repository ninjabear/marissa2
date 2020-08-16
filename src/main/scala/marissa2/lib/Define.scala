package marissa2.lib

import marissa2.models.Message
import java.net.URLEncoder

import com.typesafe.scalalogging.Logger
import marissa2.lib.urbandictionary.{Definition, Definitions}

import scala.util.{Failure, Success, Try}
import argonaut._
import Argonaut._
import scalaj.http.{Http, HttpOptions}

object Define {

  private val logger = Logger("Define")

  private def parseResult(jsonString: String): Try[List[Definition]] = Try {
    jsonString
      .decode[Definitions]
      .right
      .get
      .list
  }

  private def get(url: String): Try[String] = {
    val resp = Http(url)
      .option(HttpOptions
        .followRedirects(true)
      )
      .asString

    resp.code match {
      case 200 =>
        Success(resp.body)
      case code =>
        Failure(new IllegalStateException(s"$url returned non 200: ${code}"))
    }
  }

  def apply(message:Message, get: (String) => Try[String] = get): Unit = {
    if (message.contents.startsWith("define")) {
      logger.debug("logger contains define")
      logger.debug(message.contents)
      val queryText = message.contents.replaceFirst("define\\s+","")
      logger.debug(queryText)
      val notARealWord = s"Sorry.. I don't think $queryText is a real word."

      get(
        "http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(queryText, "UTF-8")
      ).flatMap(parseResult) match {
        case Success(definition :: _) => message.reply(definition.description)
        case Success(_) => message.reply(notARealWord)
        case Failure(exception) =>
          logger.error(s"failed to grok '$queryText'", exception)
          message.reply(notARealWord)
      }
    }
  }

}

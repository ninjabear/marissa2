package marissa2.lib

import marissa2.models.Message
import scalaj.http.Http

import scala.util.Random
import scala.util.matching.Regex

object DuckDuckGoSearch {

  private val findNuggie: Regex = """.*vqd=([\d-]+)&.*""".r
  private val imageGrok = """"image"\s*:\s*"([^"]+)"""".r
  private val hasImageKeywords = """.*image\s+(me)?\s+(.*)""".r

  private def ddGoSearch(keywords: String): List[String] = {
    val url = "https://duckduckgo.com/"
    val kwParams = Map("q" -> keywords)

    val res = Http(url).postForm(kwParams.toSeq).asString

    val body = res.body
    val specialNugget = body match {
      case findNuggie(n) => n
      case _ => throw new IllegalStateException("no nugget in " + body)
    }

    val headers = Map(
      "authority" -> "duckduckgo.com",
      "accept" -> "application/json, text/javascript, */*; q=0.01",
      "sec-fetch-dest" -> "empty",
      "x-requested-with" -> "XMLHttpRequest",
      "user-agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36",
      "sec-fetch-site" -> "same-origin",
      "sec-fetch-mode" -> "cors",
      "referer" -> "https://duckduckgo.com/",
      "accept-language" -> "en-US,en;q=0.9",
    )

    val params: Map[String, String] = Map(
      "l" -> "us-en",
      "o" -> "json",
      "q" -> keywords,
      "vqd" -> specialNugget,
      "f" -> ",,,",
      "p" -> "1",
      "v7exp" -> "a"
    )

    val requestUrl = url + "i.js";

    val resp = Http(requestUrl).headers(headers).params(params)
    val s = resp.asString

    imageGrok.findAllIn(s.body).matchData map {
      _.group(1)
    } toList
  }

  def searchImages(m: Message): Unit = {
    m.contents match {
      case hasImageKeywords(_, keywords) => {
        val results = ddGoSearch(keywords)
        if (results.nonEmpty) {
          m.reply(Random.shuffle(results.take(5)).head)
        } else {
          m.reply("I did a look but I do not never did find anything")
        }
      }
      case _ =>
    }
  }

}

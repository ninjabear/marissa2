package marissa2.lib

import helpers.BaseSpec
import marissa2.models.Message
import scalaj.http.{Http, HttpOptions}

import scala.util.Try


class DuckDuckGoSpec extends BaseSpec {

  "the search function" should {

    "do nothing if it doesn't start with image" in {
      val get = mockFunction[String, Try[String]]
      val reply = mockFunction[String,Unit]

      get.expects(*).never()
      reply.expects(*).never()

      DuckDuckGoSearch.searchImages(Message(None, "foo bar", reply))
    }

    "return an url that is 2xx" in {
      def reply(url:String) = {
        val resp = Http(url)
          .method("HEAD")
          .option(HttpOptions
            .followRedirects(true)
          ).asString

        resp.is2xx should be (true)
        resp.contentType.get should startWith ("image/")
      }
      DuckDuckGoSearch.searchImages(Message(None, "image me kirk", reply))
    }

  }

}

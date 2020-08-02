package lib

import helpers.BaseSpec
import marissa2.models.Message
import scalaj.http.{Http, HttpOptions, HttpResponse}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class SelfieSpec extends BaseSpec {

  import scala.concurrent.ExecutionContext.Implicits.global

  private def dummyMessage(message: String, reply: (String) => Unit) = Message(from = None, message, reply)

  "the selfie should be sent back" in {
    val reply = mockFunction[String, Unit]
    val selfie = "selfie"
    reply.expects(selfie).returning(()).atLeastOnce()
    Selfie.selfie(
      dummyMessage("selfie", reply),
      () => selfie
    )
  }

  "the selfie should only be sent if selfie is present" in {
    val selfie = () => "foo"
    val reply = mockFunction[String, Unit]
    reply.expects("foo").never()

    Selfie.selfie(
      dummyMessage("foo", reply),
      selfie
    )
  }

  "random selfies" should {

    "have multiple available" in {
      val urls = (1 to 100) map {
        Selfie.randomSelfieUrl()
      }.toSet
      urls.size should be > 1
    }

    "the selfies should all be available and respond" in {
      val selfies = Selfie.selfies.map {
        url =>
          Future {
            Http(url)
              .method("HEAD")
              .option(HttpOptions
                .followRedirects(true)
              ).asString
          }
      }
      forAll(selfies) {
        resp => {
          val url = Await.result(resp, Duration.Inf)
          withClue(s"${url}") {
            url.code should equal(200)
          }
        }
      }
    }


  }


}

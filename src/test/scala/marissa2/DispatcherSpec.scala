package marissa2

import com.typesafe.scalalogging.Logger
import helpers.BaseSpec
import marissa2.models.Message

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Failure
import org.scalamock.scalatest._

class DispatcherSpec extends BaseSpec {

  implicit val logger: Logger = Logger("DispatcherSpec")

  def mockMessage = Message(None, "hello world", (_) => {})

  "dispatching" should {

    "trigger all the supplied functions" in {
      val funcs = mockFunction[Message, Unit]
      val message = mockMessage
      funcs.expects(message).once()

      val f: Future[Unit] = Dispatcher(Seq(funcs)).dispatch(mockMessage)
      Await.ready(f, Duration.Inf)
    }

    "fail if a future fails" in {
      val exception = new IllegalArgumentException("???")
      val func:(Message) => Unit = (_) => {
        throw exception
      }
      val message = mockMessage
      val f = Dispatcher(Seq(func)).dispatch(mockMessage)
      Await.ready(f, Duration.Inf).value.get should equal (Failure(exception))
    }

  }

}

package marissa2.lib

import helpers.BaseSpec
import marissa2.models.Message

class PingSpec extends BaseSpec {

  "ping" should {

    "reply pong" in {
      val reply = mockFunction[String, Unit]
      reply expects "pong"
      Ping.ping(Message(None, "ping", reply))
    }

    "ignore pong" in {
      val reply = (_: String) => throw new IllegalStateException("failed")
      Ping.ping(Message(None, "pong", reply))
    }

    "ignore ping if it's in a word" in {
      val reply = (_: String) => throw new IllegalStateException("failed")
      Ping.ping(Message(None, "such high ping!", reply))
    }

  }

}

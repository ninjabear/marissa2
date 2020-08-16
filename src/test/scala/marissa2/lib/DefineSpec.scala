package marissa2.lib

import helpers.BaseSpec
import marissa2.models.Message

import scala.collection.immutable.Stack
import scala.util.{Failure, Success, Try}

class DefineSpec extends BaseSpec {

  private def goodResp(url:String): Try[String] = {
    Success("""{
      "list": [
      {
        "definition": "[The Foo] were a species that terrorized the earth for many [eons], until [the Foo Fighters] appeared, and drove the species to extinction.",
        "permalink": "http://foo.urbanup.com/14736869",
        "thumbs_up": 19,
        "sound_urls": [],
        "author": "The foo fighter",
        "word": "Foo",
        "defid": 14736869,
        "current_vote": "",
        "written_on": "2020-02-20T00:00:00.000Z",
        "example": "“What is [a Foo]?”\r\n“[Tf] do you mean?”\r\n“[Never mind]”",
        "thumbs_down": 1
      },
      {
        "definition": "A slang word commonly used by the [Hispanic] population to [identify] a friend or [homie].",
        "permalink": "http://foo.urbanup.com/14024105",
        "thumbs_up": 166,
        "sound_urls": [],
        "author": "mandashwell",
        "word": "foo",
        "defid": 14024105,
        "current_vote": "",
        "written_on": "2019-06-27T00:00:00.000Z",
        "example": "[What's up] foo.",
        "thumbs_down": 19
      }
      ]
    }""")
  }

  "definitions" should {

    "return the top response" in {
      val reply = mockFunction[String,Unit]
      reply expects ("[The Foo] were a species that terrorized the earth for many [eons], until [the Foo Fighters] appeared, and drove the species to extinction.")
      Define(Message(None, "define foo", reply), goodResp)
    }

    "say sorry if nothing comes back" in {
      val reply = mockFunction[String,Unit]
      reply expects ("Sorry.. I don't think foo is a real word.")
      Define(Message(None, "define foo", reply), _=>util.Failure(new IllegalArgumentException("???")))
    }

    "use the correctly formatted url" in {
      val get = mockFunction[String,Try[String]]
      get.expects("http://api.urbandictionary.com/v0/define?term=foo+bar").returning(Success("???")).once
      Define(Message(None, "define foo bar", _=>Unit), get)
    }

    "return something for real" in {
      class store {
        var stack: List[String] = List[String]()
        def pop(s:String):Unit = stack = s :: stack
      }
      val s = new store()
      Define(Message(None, "define foo bar", s.pop))
      s.stack.head contains "foo"
    }

  }

  "skipping" should {

    "do nothing if the phrase doesn't contain define" in {
      val reply = mockFunction[String,Unit]
      reply.expects(*).never
      Define(Message(None, "foo", reply), (_)=>Failure(new RuntimeException("???")))
    }

    "do nothing if the phrase doesn't start with define" in {
      val reply = mockFunction[String,Unit]
      reply.expects(*).never
      Define(Message(None, "foo define foo", reply), (_)=>Failure(new RuntimeException("???")))
    }

  }

}

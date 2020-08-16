package marissa2

import helpers.BaseSpec
import marissa2.models.Message

import scala.language.postfixOps

class NameFilterSpec extends BaseSpec {

  val filterName: String = "marissa"

  private def withName(s: String) = s"$filterName $s"
  private def withMessageTransformed(m: Message, t:(String)=>String) = m.copy(contents = t(m.contents))
  private def withName(m: Message): Message = withMessageTransformed(m, withName)
  private def noReply = (_:String) => ()

  "filtering the name" should {

    "remove the name in the message" in {
      val m = Message(None, "foo", noReply)
      val f = mockFunction[Message, Unit]
      f expects (m) once

      NameFilter(f)(withName(m))
    }

    "be relaxed about capitalization" in {
      val m = Message(None, "foo", noReply)
      val f = mockFunction[Message, Unit]
      f expects (m) once

      NameFilter(f)(withMessageTransformed(m, (t)=>{s"${filterName.toUpperCase} $t"}))
    }

    "strip as much whitespace as there is after the name" in {
      val m = Message(None, "foo", noReply)
      val f = mockFunction[Message, Unit]
      f expects (m) once

      NameFilter(f)(withMessageTransformed(m, (t)=>{s"${filterName}                   $t"}))
    }

    "leave non leading whitespace alone" in {
      val m = Message(None, "foo bar foo", noReply)
      val f = mockFunction[Message, Unit]
      f expects (m) once

      NameFilter(f)(withName(m))
    }

  }

  "filtering the calls" should {

    "not call the function if it doesn't have the name" in {
      val f = mockFunction[Message, Unit]
      f.expects(*).never()
      NameFilter(f)(Message(None, "this does not contain the name", noReply))
    }

    "not call the function if the name isn't at the beginning" in {
      val f = mockFunction[Message, Unit]
      f.expects(*).never()
      NameFilter(f)(Message(None, "I think marissa is great", noReply))
    }

  }

}

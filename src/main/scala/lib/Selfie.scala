package lib

import marissa2.models.Message

import scala.util.Random

object Selfie {

  val selfies: List[String] = List(
    "http://i.huffpost.com/gen/882663/images/o-MARISSA-MAYER-facebook.jpg",
    "http://static.businessinsider.com/image/5213c32cecad045804000016/image.jpg",
    "http://static.businessinsider.com/image/5213c32cecad045804000016/image.jpg",
    "http://i2.cdn.turner.com/money/dam/assets/130416164248-marissa-mayer-620xa.png",
    "http://wpuploads.appadvice.com/wp-content/uploads/2013/05/marissa-mayer-yahoo-new-c-008.jpg",
    "https://pbs.twimg.com/profile_images/323982494/marissa_new4.jpg",
    "http://media.idownloadblog.com/wp-content/uploads/2015/01/Marissa-Mayer-Yahoo-001.jpg"
  )

  val matchText = "selfie"

  def randomSelfieUrl(): String = selfies(Random.nextInt(selfies.length))

  def selfie(m: Message, getSelfie: () => String = randomSelfieUrl): Unit = {
    if (m.message.contains(matchText))
      m.reply(getSelfie())
  }

}


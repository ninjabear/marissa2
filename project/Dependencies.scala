import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.0"
  lazy val scalaMock = "org.scalamock" %% "scalamock" % "4.4.0"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  lazy val discord4j = "com.discord4j" % "discord4j-core" % "3.0.14"
  lazy val scalaj = "org.scalaj" %% "scalaj-http" % "2.4.2"
}

import Dependencies._

enablePlugins(sbtdocker.DockerPlugin)

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.ninjabear"
ThisBuild / organizationName := "marissa2"

lazy val root = (project in file("."))
  .settings(
    name := "marissa2",
    libraryDependencies ++= Seq(
      logback,
      scalaLogging,
      discord4j,
      scalaTest % Test
    )
  )

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("openjdk:8-jre")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

assemblyMergeStrategy in assembly := {
  case "module-info.class" => MergeStrategy.discard
  case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

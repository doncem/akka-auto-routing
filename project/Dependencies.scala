import sbt._

object Dependencies {

  private val akkaV = "10.0.2"
  private val circeV = "0.7.0"

  /*
  entities ------ core ------ akka
   */
  lazy val entities: Seq[ModuleID] = Seq("com.typesafe" % "config" % "1.3.1")
  lazy val routing: Seq[ModuleID] = circe ++ logging ++ tests
  lazy val akka: Seq[ModuleID] = http

  private val circe = Seq(
    "io.circe" %% "circe-core"    % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser"  % circeV
  )

  private val http = Seq(
    "com.typesafe.akka" %% "akka-http"          % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit"  % akkaV % Test,
    "de.heikoseeberger" %% "akka-http-circe"    % "1.12.0"
  )

  private val logging = Seq(
    "ch.qos.logback"             % "logback-classic"  % "1.1.9",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.5.0"
  )

  private val tests = Seq(
    "org.scalatest" %% "scalatest"  % "3.0.1" % Test
  )

}

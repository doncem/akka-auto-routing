import sbt.Keys._

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.12.1"
)

lazy val entities = (project in file("entities"))
  .settings(commonSettings: _*)
  .settings(
    name := "routing-entities",
    libraryDependencies ++= Dependencies.entities
  )

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "routing-core",
    libraryDependencies ++= Dependencies.routing
  )
  .dependsOn(entities % "test->test;compile->compile")

lazy val akka = (project in file("akka"))
  .settings(commonSettings: _*)
  .settings(
    name := "routing-akka",
    libraryDependencies ++= Dependencies.akka
  )
  .dependsOn(core % "test->test;compile->compile")

lazy val routing = (project in file("."))
  .settings(commonSettings: _*)
  .settings(Revolver.settings: _*)
  .settings(name := "routing")
  .aggregate(entities, core, akka)

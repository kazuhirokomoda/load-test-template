name := "load-test-template"
version := "0.1"
scalaVersion := "2.12.9"

val gatlingVersion = "3.2.1"
libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % gatlingVersion % "provided",
  "io.gatling" % "gatling-http" % gatlingVersion % "provided"
)
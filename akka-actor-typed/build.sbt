name := "akka-actor-typed"

version := "0.1"

scalaVersion := "2.13.4"

val AkkaVersion = "2.6.10"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
)
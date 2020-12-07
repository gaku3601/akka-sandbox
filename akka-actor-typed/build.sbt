name := "akka-actor-typed"

version := "0.1"

scalaVersion := "2.13.4"

val AkkaVersion = "2.6.10"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "org.slf4j" % "slf4j-log4j12" % "1.7.30",
)

name := "akka-cluster-example"

version := "1.0"

scalaVersion := "2.13.1"

lazy val akkaVersion = "2.6.18"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

assembly / mainClass := Some("njustus.clusterexample.Main")
assembly / assemblyJarName := "cluster-example.jar"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.javafaker" % "javafaker" % "0.15",
//  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

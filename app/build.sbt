name := """app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.apache.httpcomponents" % "fluent-hc" % "4.4.1",
  "com.google.code.crawler-commons" % "crawler-commons" % "0.5"
)


fork in run := true
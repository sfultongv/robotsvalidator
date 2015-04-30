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
  "org.archive.heritrix" % "heritrix" % "3.2.0",
  "commons-io" % "commons-io" % "2.4",
  "com.google.guava" % "guava" % "18.0"
)


fork in run := true
import sbt.Keys._

lazy val commonSettings = Seq(
  organization := "com.tablo",
  version := "2.0.0",
  scalaVersion := "2.11.5",
  exportJars := true,
  // This forbids including Scala related libraries into the dependency
  autoScalaLibrary := false,
  // Enables publishing to maven repo
  publishMavenStyle := true,
  // Do not append Scala versions to the generated artifacts
  crossPaths := false
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "tablo"
  )

libraryDependencies ++= Seq(
  "com.pi4j" % "pi4j-core" % "1.0",
  "com.pi4j" % "pi4j-device" % "1.0",
  "log4j" % "log4j" % "1.2.17",
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
  "org.jboss.resteasy" % "resteasy-client" % "3.0.17.Final",
  "org.codehaus.jackson" % "jackson-smile" % "1.9.13",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"
)

mainClass in assembly := Some("com.tablo.Tablo")

assemblyMergeStrategy in assembly := {
  case "log4j.properties"	=> MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
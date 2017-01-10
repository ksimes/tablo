name := "tablo"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.pi4j" % "pi4j-core" % "1.0",
  "com.pi4j" % "pi4j-device" % "1.0",
  "log4j" % "log4j" % "1.2.16",
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
  "org.jboss.resteasy" % "resteasy-client" % "3.0.17.Final",
  "org.codehaus.jackson" % "jackson-smile" % "1.9.13",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"
)
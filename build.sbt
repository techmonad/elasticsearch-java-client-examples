name := "elasticsearch-java-client-examples"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.elasticsearch.client" % "transport" % "6.3.2",
  "org.json4s" %% "json4s-native" % "3.5.4",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.3.3"
)

package placid

import sbt._

object Dependencies {

  object Versions {

    def crossScalaVersions = Seq("2.11.6", "2.10.5")

    def scalaVersion = crossScalaVersions.head

    def sourceJavaVersion = "1.7"

    def targetJavaVersion = "1.7"

    def targetJvmVersion = "1.7"

    def camelVersion = "2.14.1"

    def activemqVersion = "5.11.0"

    def slickVersion = "3.0.0-RC1"

  }

  object Compile {

    import Versions._

    def compile(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "compile")
    
    def provided(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "provided")
    
    def optional(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "optional")
    
    def test(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "test")
    
    def runtime(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "runtime")
    
    def container(deps: Seq[ModuleID]): Seq[ModuleID] = deps map (_ % "container")

    def config = Seq(
      "com.typesafe" % "config" % "1.2.1")

    def logging = Seq(
      "org.slf4j" % "slf4j-api" % "1.7.10",
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.2",
      "org.apache.logging.log4j" % "log4j-api" % "2.2",
      "org.apache.logging.log4j" % "log4j-core" % "2.2")

    def reflection = Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion,
      "org.scala-lang" % "scala-reflect" % scalaVersion,
      "org.reflections" % "reflections" % "0.9.9")

    def scalamodules = Seq(
      "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3")

    def joda = Seq(
      "joda-time" % "joda-time" % "2.7")

    def javax = Seq(
      "javax.servlet" % "javax.servlet-api" % "3.1.0",
      "javax.servlet" % "jstl" % "1.2",
      "org.glassfish.web" % "javax.servlet.jsp" % "2.3.2")

    def compression = Seq(
      "net.jpountz.lz4" % "lz4" % "1.3.0",
      "net.lingala.zip4j" % "zip4j" % "1.3.2")

    def codec = Seq(
      "org.scodec" %% "scodec-core" % "1.7.0",
      "org.scodec" %% "scodec-scalaz" % "1.0.0",
      "org.scodec" %% "scodec-bits" % "1.0.5")

    def json = Seq(
      "org.json4s" %% "json4s-native" % "3.2.11",
      "org.json4s" %% "json4s-jackson" % "3.2.11",
      "org.json4s" %% "json4s-ext" % "3.2.11",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.1",
      "com.sun.jersey" % "jersey-json" % "1.18.3")

    def slick = Seq(
      "com.typesafe.slick" %% "slick-extensions" % slickVersion,
      "com.typesafe.slick" %% "slick" % slickVersion)

    def apachecommons = Seq(
      "org.apache.commons" % "commons-lang3" % "3.3.2",
      "org.apache.commons" % "commons-compress" % "1.9",
      "org.apache.commons" % "commons-exec" % "1.2",
      "commons-io" % "commons-io" % "2.4",
      "commons-net" % "commons-net" % "3.3",
      "commons-codec" % "commons-codec" % "1.10",
      "org.apache.httpcomponents" % "httpclient" % "4.3.6")

    def camel = Seq(
      "org.apache.camel" % "camel-core" % camelVersion,
      "org.apache.camel" % "camel-scala" % camelVersion,
      "org.apache.camel" % "camel-exec" % camelVersion exclude ("commons-exec", "commons-exec"),
      "org.apache.camel" % "camel-ssh" % camelVersion,
      "org.apache.camel" % "camel-stream" % camelVersion,
      "org.apache.camel" % "camel-zipfile" % camelVersion,
      "org.apache.camel" % "camel-quartz2" % camelVersion,
      "org.apache.camel" % "camel-sjms" % camelVersion,
      "org.apache.camel" % "camel-mail" % camelVersion,
      "org.apache.camel" % "camel-ahc" % camelVersion,
      "org.apache.camel" % "camel-ldap" % camelVersion,
      "org.apache.camel" % "camel-servlet" % camelVersion,
      "org.apache.camel" % "camel-leveldb" % camelVersion)

    def activemq = Seq(
      "org.apache.activemq" % "activemq-camel" % activemqVersion,
      "org.apache.activemq" % "activemq-broker" % activemqVersion,
      "org.apache.activemq" % "activemq-jaas" % activemqVersion,
      "org.apache.activemq" % "activemq-kahadb-store" % activemqVersion)

    def scalatest = Seq(
      "org.scalatest" %% "scalatest" % "2.2.4",
      "org.scalacheck" %% "scalacheck" % "1.12.2")

    def oracleJdbc11 = Seq(
      "com.oracle" % "ojdbc6" % "11.2.0.4")

    def microsoftJdbc4 = Seq(
      "com.microsoft.sqlserver" % "sqljdbc4" % "4.0")

  }

  import Compile._

  def util = apachecommons ++ compression ++ joda ++ reflection

  def aio = codec ++ compression

}


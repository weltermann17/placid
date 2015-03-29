package placid

import Dependencies._
import Dependencies.Compile._
import Dependencies.Compile.{ test ⇒ forTest }
import Settings._

import sbt._
import sbt.Keys._

object CompleteBuild

    extends Build {

  def buildSettings = Seq(
    organization := "pleasant",
    scalaVersion := Dependencies.Versions.scalaVersion,
    crossScalaVersions := Dependencies.Versions.crossScalaVersions)

  def defaultSettings = buildSettings ++ baseSettings

  lazy val root = Project("root", file("."))
    .aggregate(aio, util)

  lazy val aio = Project("pleasant-aio", file("aio"))
    .settings(defaultSettings)
    .settings(libraryDependencies ++= Dependencies.aio ++ forTest(scalatest))

  lazy val util = Project("pleasant-util", file("util"))
    .settings(defaultSettings)
    .settings(libraryDependencies ++= Dependencies.util ++ forTest(scalatest))


}

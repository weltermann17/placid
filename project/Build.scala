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
    organization := "placid",
    scalaVersion := Dependencies.Versions.scalaVersion,
    crossScalaVersions := Dependencies.Versions.crossScalaVersions)

  def defaultSettings = buildSettings ++ baseSettings

  lazy val root = Project("root", file("."))
    .aggregate(util)

  lazy val util = Project("placid-util", file("util"))
    .settings(defaultSettings: _*)
    .settings(Seq(version in ThisBuild := "0.0.1-SNAPSHOT"): _*)
    .settings(libraryDependencies ++= Dependencies.util ++ forTest(scalatest))

}
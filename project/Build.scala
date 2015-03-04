package placid

import Dependencies._
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

  lazy val root = Project(
    id = "root",
    base = file("."),
    aggregate = Seq(util))

  lazy val util = Project(
    id = "placid-util",
    base = file("util"),
    settings = defaultSettings ++ Seq(version in ThisBuild := "0.0.1-SNAPSHOT"))

}
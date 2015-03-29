package placid

import Dependencies.Versions._

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys._

import net.virtualvoid.sbt.graph.Plugin.graphSettings

import sbtrelease.ReleasePlugin.releaseSettings

import sbt._
import sbt.Keys._

object Settings {

  def baseSettings = compileSettings ++
    eclipseSettings ++
    releaseSettings ++
    graphSettings ++
    FormatSettings.formatSettings ++
    Seq.empty

  def compileSettings = Seq(
    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    scalacOptions ++= Seq(
      "-g:vars",
      "-encoding", "UTF-8",
      "-target:jvm-" + targetJvmVersion,
      "-deprecation",
      "-feature",
      "-Yinline-warnings",
      "-Yno-generic-signatures",
      "-optimize",
      "-nowarn",
      "-unchecked"),
    scalacOptions in (doc) := Seq(
      "-diagrams",
      "-doc-title <none>",
      "-access private"),
    javacOptions ++= Seq(
      "-source", sourceJavaVersion,
      "-target", targetJavaVersion,
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Xlint:-options"))

  object FormatSettings {

    def formatSettings = SbtScalariform.scalariformSettings ++ Seq(
      ScalariformKeys.preferences in Compile := formattingPreferences,
      ScalariformKeys.preferences in MultiJvm := formattingPreferences,
      ScalariformKeys.preferences in Test := formattingPreferences)

    def docFormatSettings = SbtScalariform.scalariformSettings ++ Seq(
      ScalariformKeys.preferences in Compile := docFormattingPreferences,
      ScalariformKeys.preferences in MultiJvm := docFormattingPreferences,
      ScalariformKeys.preferences in Test := docFormattingPreferences)

    def formattingPreferences = {
      import scalariform.formatter.preferences._
      FormattingPreferences()
        .setPreference(RewriteArrowSymbols, true)
        .setPreference(AlignSingleLineCaseStatements, false)
        .setPreference(AlignParameters, false)
        .setPreference(DoubleIndentClassDeclaration, true)
    }

    def docFormattingPreferences = {
      import scalariform.formatter.preferences._
      formattingPreferences.setPreference(RewriteArrowSymbols, false)
    }

  }

  def eclipseSettings = Seq(
    createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
    eclipseOutput := Some("target/scala-2.11/classes"),
    withSource := true,
    incOptions := incOptions.value.withNameHashing(true))

}

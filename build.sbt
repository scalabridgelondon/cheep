name := "cheep"

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / useSuperShell := false

// ScalaFix configuration
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

val catsVersion = "2.4.2"
val circeVersion = "0.13.0"
val http4sVersion = "1.0.0-M16"
val logbackVersion = "1.2.3"

val sharedSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"     % catsVersion,
    "io.circe"      %%% "circe-core"    % circeVersion,
    "io.circe"      %%% "circe-generic" % circeVersion,
    "io.circe"      %%% "circe-parser"  % circeVersion,
    "org.scalameta" %%% "munit"         % "0.7.22" % Test
  ),
  scalacOptions ++= Seq(
    "-Yrangepos",
    "-Ymacro-annotations",
    "-Wunused:imports",
    "-Werror"
  ),
  testFrameworks += new TestFramework("munit.Framework"),
  addCompilerPlugin(scalafixSemanticdb)
)

val deploy = taskKey[Unit]("Deploy the frontend to the backend asset location")
val build = taskKey[Unit]("Format, compile, and test")

lazy val data = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("data"))
  .settings(
    sharedSettings,
    build := { Def.sequential(scalafixAll.toTask(""), scalafmtAll, Test / test).value }
  )

lazy val backend = project
  .in(file("backend"))
  .settings(
    sharedSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "ch.qos.logback" %  "logback-classic" % logbackVersion,
      ),
    run / fork := true,
    run / javaOptions += s"-Dtodone.assets=${((baseDirectory.value) / "assets").toString}",
    build := { Def.sequential(scalafixAll.toTask(""), scalafmtAll, Test / test).value }
  )
  .dependsOn(data.jvm)


lazy val frontend = project
  .in(file("frontend"))
  .settings(
    sharedSettings,
    Compile / npmDependencies ++= Seq(
      "react" -> "16.13.1",
      "react-dom" -> "16.13.1",
      "react-proxy" -> "1.1.8"
    ),
    Compile / npmDevDependencies ++= Seq(
      "file-loader" -> "6.0.0",
      "style-loader" -> "1.2.1",
      "css-loader" -> "3.5.3",
      "html-webpack-plugin" -> "4.3.0",
      "copy-webpack-plugin" -> "5.1.1",
      "webpack-merge" -> "4.2.2",
      "postcss-loader" -> "4.1.0",
      "postcss" -> "8.2.6",
      "tailwindcss" -> "2.0.1",
      "autoprefixer" -> "10.0.2",
      "react-icons" -> "4.1.0"
    ),
    libraryDependencies ++= Seq(
      "me.shadaj" %%% "slinky-web" % "0.6.7",
      "me.shadaj" %%% "slinky-hot" % "0.6.7",
      "org.scala-js" %%% "scalajs-dom" % "1.0.0"
    ),
    webpack / version := "4.43.0",
    startWebpackDevServer / version := "3.11.0",
    webpackResources := baseDirectory.value / "webpack" * "*",
    fastOptJS / webpackConfigFile := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js"),
    fastOptJS / webpackDevServerExtraArgs := Seq("--inline", "--hot"),
    fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly(),
    fullOptJS / webpackConfigFile := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js"),
    Test / webpackConfigFile := Some(baseDirectory.value / "webpack" / "webpack-core.config.js"),
    Test / requireJsDomEnv := true,
    build := { Def.sequential(scalafixAll.toTask(""), scalafmtAll, Test / test, Compile / fullOptJS, deploy).value },
    deploy := {
      val fs = (Compile / fullOptJS / webpack).value
      val outDir = (backend / baseDirectory).value / "assets"

      fs.foreach { f =>
        val input = f.data
        val output = outDir / (f.data.name)
        println(s"Deploying $input to $output")
        sbt.io.IO.copyFile(input, output)}
    }
  )
  .enablePlugins(ScalaJSBundlerPlugin)
  .dependsOn(data.js)

addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS")

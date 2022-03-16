import Dependencies._

ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions := Seq(
  "-encoding",
  "utf8",
  "-deprecation",
  "-Xfatal-warnings",
  "-Xlint:infer-any",
  "-Xlint:missing-interpolator",
  "-Xlint:package-object-classes",
  "-Xlint:unused"
)

lazy val formatAll = taskKey[Unit]("Format all the source code which includes src, test, and build files")
lazy val checkFormat = taskKey[Unit]("Check all the source code which includes src, test, and build files")

addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheckAll")

lazy val commonSettings = Seq(
  formatAll := {
    (Compile / scalafmt).value
    (Test / scalafmt).value
  },
  checkFormat := {
    (Compile / scalafmtCheck).value
    (Test / scalafmtCheck).value
  },
  Compile / compile := (Compile / compile).dependsOn(checkFormat).value,
  Test / test := (Test / test).dependsOn(checkFormat).value
)

lazy val root = (project in file("."))
  .settings(
    name := "Random Coffee",
    commonSettings
  )
  .aggregate(bootstrap)

lazy val bootstrap = project
  .settings(
    name := "Bootstrap",
    libraryDependencies ++= akkaHttp :+ akkaActor,
    commonSettings,
    Compile / mainClass := Some("com.epam.random_coffee.RandomCoffeeApp")
  )
  .aggregate(`auth-service`, `rc-events-service`)
  .dependsOn(`auth-service`, `rc-events-service`)

lazy val `auth-service` =
  (project in file("auth-service"))
    .settings(
      libraryDependencies ++= akkaHttpBase +: (baseTest ++ akkaHttpTest),
      commonSettings
    )
    .dependsOn(`auth-service-api`)

lazy val `auth-service-api` =
  (project in file("auth-service-api")).settings(
    libraryDependencies += akkaHttpBase,
    commonSettings
  )

lazy val `rc-events-service` =
  (project in file("rc-events-service"))
    .settings(
      libraryDependencies ++= akkaHttpBase +: (baseTest ++ akkaHttpTest),
      commonSettings
    )
    .dependsOn(`rc-events-service-api`)

lazy val `rc-events-service-api` =
  (project in file("rc-events-service-api")).settings(
    libraryDependencies += akkaHttpBase,
    commonSettings
  )

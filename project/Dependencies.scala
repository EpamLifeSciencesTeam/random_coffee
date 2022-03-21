import sbt._

object Dependencies {

  object Version {
    // main
    val akka = "2.6.18"
    val akkaHttp = "10.2.9"
    val akkaHttpJson = "1.39.2"
    val circe = "0.14.1"
    val doobie = "1.0.0-RC2"
    val jwtCore = "5.0.0"
    val liquibase = "4.9.0"
    val logback = "1.2.11"
    val pureconfig = "0.17.1"
    val scalaLogging = "3.9.4"
    val slf4j = "1.7.36"

    // test
    val scalaMock = "5.2.0"
    val scalaTest = "3.2.11"
  }

  // main
  lazy val akkaHttp = Seq(akkaHttpBase, akkaStreams)
  lazy val doobie = Seq(doobieCore, doobiePg, doobieHikari)
  lazy val circe = Seq(circeCore, circeGeneric, circeParser)
  lazy val logging = Seq(scalaLogging, slf4jApi, logback)

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaHttpBase = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
  val akkaHttpJson = "de.heikoseeberger" %% "akka-http-circe" % Version.akkaHttpJson

  val circeCore = "io.circe" %% "circe-core" % Version.circe
  val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
  val circeParser = "io.circe" %% "circe-parser" % Version.circe

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobiePg = "org.tpolecat" %% "doobie-postgres" % Version.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Version.doobie

  val jwtCore = "com.pauldijou" %% "jwt-core" % Version.jwtCore

  val liquibase = "org.liquibase" % "liquibase-core" % Version.liquibase

  val logback = "ch.qos.logback" % "logback-classic" % Version.logback

  val pureconfig = "com.github.pureconfig" %% "pureconfig" % Version.pureconfig

  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging
  val slf4jApi = "org.slf4j" % "slf4j-api" % Version.slf4j

  // test
  // todo remove akkaStreams once it is used in main classes
  lazy val akkaHttpTest = Seq(akkaTestKit, akkaHttpTestKit, akkaStreams % Test)
  lazy val baseTest = Seq(scalaTest, scalaMock)

  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka % Test
  val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % Test

  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % Test
  val scalaMock = "org.scalamock" %% "scalamock" % Version.scalaMock % Test

}

import sbt._

object Dependencies {

  object Version {
    // main
    val akka = "2.6.18"
    val akkaHttp = "10.2.9"
    val circe = "0.14.1"
    val doobie = "0.13.4"
    val pureconfig = "0.17.1"

    // test
    val scalaTest = "3.2.11"
  }

  // main
  lazy val akkaHttp = Seq(akkaHttpBase, akkaStreams)
  lazy val doobie = Seq(doobieCore, doobiePg, doobieHikari)
  lazy val circe = Seq(circeCore, circeGeneric, circeParser)

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaHttpBase = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp

  val circeCore = "io.circe" %% "circe-core" % Version.circe
  val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
  val circeParser = "io.circe" %% "circe-parser" % Version.circe

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobiePg = "org.tpolecat" %% "doobie-postgres" % Version.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Version.doobie

  val pureconfig = "com.github.pureconfig" %% "pureconfig" % Version.pureconfig

  // test
  // todo remove akkaStreams once it is used in main classes
  lazy val akkaHttpTest = Seq(akkaTestKit, akkaHttpTestKit, akkaStreams % "test")
  lazy val baseTest = Seq(scalaTest)

  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka % "test"
  val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % "test"

  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "test"

}

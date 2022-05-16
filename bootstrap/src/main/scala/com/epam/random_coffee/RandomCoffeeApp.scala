package com.epam.random_coffee

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.epam.random_coffee.authentication.AuthenticationApp
import com.epam.random_coffee.authentication.config.AuthenticationServiceConfig
import com.epam.random_coffee.config.RandomCoffeeConfig
import com.epam.random_coffee.events.EventApp
import com.epam.random_coffee.events.config.EventServiceConfig
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException

import scala.util.{ Failure, Success, Try }

object RandomCoffeeApp extends App {

  private implicit val system: ActorSystem = ActorSystem("random-coffee")

  private def loadConfig(): Try[RandomCoffeeConfig] =
    ConfigSource.default.load[RandomCoffeeConfig].left.map(ConfigReaderException[RandomCoffeeConfig](_)).toTry

  private def loadAuthenticationApi(config: AuthenticationServiceConfig): Try[Route] = {
    val authenticationApp = new AuthenticationApp(config)
    authenticationApp.init.flatMap(_ => authenticationApp.api)
  }
  private def loadEventApi(config: EventServiceConfig): Try[Route] = {
    val eventApp = new EventApp(config)
    eventApp.init.flatMap(_ => eventApp.api)
  }

  val app =
    for {
      config <- loadConfig()
      authRoutes <- loadAuthenticationApi(config.authentication)
      rcEventsRoutes <- loadEventApi(config.eventService)
    } yield {
      val route = Route.seal(authRoutes ~ rcEventsRoutes)

      // todo take values from a config
      val interface = "localhost"
      val port = 8088
      Http().newServerAt(interface, port).bind(route)
      println(s"Server online at http://$interface:$port/")
    }

  app match {
    case Failure(exception) =>
      println(s"Failed to start an app due to exception $exception")
      system.terminate()
    case Success(_) =>
  }

}

package com.epam.random_coffee

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.epam.random_coffee.authentication.api.AuthenticationAPIImpl
import com.epam.random_coffee.events.api.RcEventsAPIImpl

object RandomCoffeeApp extends App {

  private implicit val system: ActorSystem = ActorSystem("random-coffee")

  private val authRoutes = new AuthenticationAPIImpl
  private val rcEventsRoutes = new RcEventsAPIImpl

  private val route = Route.seal(authRoutes.routes ~ rcEventsRoutes.routes)

  // todo take values from a config
  val interface = "localhost"
  val port = 8088
  Http().newServerAt("localhost", 8088).bind(route)
  // todo log instead of println
  println(s"Server online at http://$interface:$port/")

}

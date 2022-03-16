package com.epam.random_coffee.events.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class RcEventsAPIImpl extends RcEventsAPI {

  override def routes: Route = pathPrefix("events" / "v1")(failWith(new NotImplementedError))

}

package com.epam.random_coffee.events.api

import akka.http.scaladsl.server.Route

trait RcEventsAPI {

  def routes: Route

}

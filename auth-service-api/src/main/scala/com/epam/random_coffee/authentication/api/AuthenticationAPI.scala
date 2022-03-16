package com.epam.random_coffee.authentication.api

import akka.http.scaladsl.server.Route

trait AuthenticationAPI {

  def routes: Route

}

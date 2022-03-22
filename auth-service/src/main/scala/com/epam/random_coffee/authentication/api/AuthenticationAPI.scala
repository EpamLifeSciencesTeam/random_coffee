package com.epam.random_coffee.authentication.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class AuthenticationAPI {

  def routes: Route = pathPrefix("auth" / "v1")(signIn ~ signUp)

  private lazy val signIn: Route = path("signIn")(failWith(new NotImplementedError))

  private lazy val signUp: Route = path("signUp")(failWith(new NotImplementedError))

}

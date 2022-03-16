package com.epam.random_coffee.authentication.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.wordspec.AnyWordSpec

class AuthenticationAPIImplSpec extends AnyWordSpec with ScalatestRouteTest {

  private val authAPI: AuthenticationAPI = new AuthenticationAPIImpl
  private val routes = Route.seal(authAPI.routes)

  "AuthenticationAPI" should {
    "fail with 500" when {
      "user attempts to sign in" in {
        Post("/auth/v1/signIn") ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }
      "user attempts to sign up" in {
        Post("/auth/v1/signUp") ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }
    }
  }

}

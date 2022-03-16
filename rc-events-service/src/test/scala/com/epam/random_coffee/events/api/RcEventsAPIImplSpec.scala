package com.epam.random_coffee.events.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.wordspec.AnyWordSpec

class RcEventsAPIImplSpec extends AnyWordSpec with ScalatestRouteTest {

  private val authAPI: RcEventsAPI = new RcEventsAPIImpl
  private val routes = Route.seal(authAPI.routes)

  "RcEventsAPI" should {
    "fail with 500" when {
      "any request comes in" in {
        Get("/events/v1") ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }
    }
  }

}

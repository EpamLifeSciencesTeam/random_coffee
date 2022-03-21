package com.epam.random_coffee.authentication.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.epam.random_coffee.authentication.api.codecs.AuthenticationCodecs._
import com.epam.random_coffee.authentication.api.request.{ SignInRequest, SignUpRequest }
import com.epam.random_coffee.authentication.api.response.SignUpResponse
import com.epam.random_coffee.authentication.model.{ AccessToken, Email, UserId }
import com.epam.random_coffee.authentication.service.AuthenticationService
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

class AuthenticationAPISpec extends AnyWordSpec with MockFactory with OneInstancePerTest with ScalatestRouteTest {

  private val authService = mock[AuthenticationService]
  private val authAPI = new AuthenticationAPI(authService)
  private val routes = Route.seal(authAPI.routes)

  private val signInRequest = SignInRequest(Email("test_email"), "test_password")
  private val signUpRequest = SignUpRequest(Email("test_email"), "test_name", "test_password")

  private val signUpResponse = SignUpResponse(UserId("test_id"), signUpRequest.email, signUpRequest.name)

  "AuthenticationAPI" should {
    "respond with an access token" when {
      "user signs in" in {
        val accessToken = AccessToken("token")

        (authService.signIn _)
          .expects(signInRequest.email, signInRequest.password)
          .returns(Future.successful(accessToken))

        Post("/auth/v1/signIn", signInRequest) ~> routes ~> check {
          assert(status == StatusCodes.NoContent)
          assert(header("Set-Authorization").map(_.value).contains(s"Bearer ${accessToken.value}"))
        }
      }
    }

    "return a newly created user" when {
      "user signs up" in {
        (authService.signUp _)
          .expects(signUpRequest.email, signUpRequest.name, signUpRequest.password)
          .returns(Future.successful(signUpResponse.id))

        Post("/auth/v1/signUp", signUpRequest) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(entityAs[SignUpResponse] == signUpResponse)
        }
      }
    }

    "fail with 500" when {
      "user's attempt to sign in has failed" in {
        (authService.signIn _).expects(*, *).returns(Future.failed(new RuntimeException))

        Post("/auth/v1/signIn", signInRequest) ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }

      "user's attempts to sign up has failed" in {
        (authService.signUp _).expects(*, *, *).returns(Future.failed(new RuntimeException))

        Post("/auth/v1/signUp", signUpRequest) ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }
    }
  }

}

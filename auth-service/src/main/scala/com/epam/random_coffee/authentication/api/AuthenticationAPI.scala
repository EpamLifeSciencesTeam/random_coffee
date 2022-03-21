package com.epam.random_coffee.authentication.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.epam.random_coffee.authentication.api.AuthenticationAPI.`Set-Authorization`
import com.epam.random_coffee.authentication.api.codecs.AuthenticationCodecs
import com.epam.random_coffee.authentication.api.request.{ SignInRequest, SignUpRequest }
import com.epam.random_coffee.authentication.api.response.SignUpResponse
import com.epam.random_coffee.authentication.service.AuthenticationService

class AuthenticationAPI(authenticationService: AuthenticationService) extends AuthenticationCodecs {

  def routes: Route = pathPrefix("auth" / "v1")(signIn ~ signUp)

  private lazy val signIn: Route =
    (path("signIn") & post & entity(as[SignInRequest])) { request =>
      onSuccess(authenticationService.signIn(request.email, request.password)) { accessToken =>
        respondWithHeaders(RawHeader(`Set-Authorization`, s"Bearer ${accessToken.value}")) {
          complete(StatusCodes.NoContent)
        }
      }
    }

  private lazy val signUp: Route =
    (path("signUp") & post & entity(as[SignUpRequest])) { request =>
      onSuccess(authenticationService.signUp(request.email, request.name, request.password)) { id =>
        complete(SignUpResponse(id, request.email, request.name))
      }
    }

}

object AuthenticationAPI {

  val `Set-Authorization` = "Set-Authorization"

}

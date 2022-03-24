package com.epam.random_coffee.authentication.api.codecs

import com.epam.random_coffee.authentication.api.request.{ SignInRequest, SignUpRequest }
import com.epam.random_coffee.authentication.api.response.SignUpResponse
import com.epam.random_coffee.authentication.json.ModelCodecs
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

trait AuthenticationCodecs extends ModelCodecs with FailFastCirceSupport {

  implicit lazy val signInRequestCodec: Codec[SignInRequest] = deriveCodec

  implicit lazy val signUpRequestCodec: Codec[SignUpRequest] = deriveCodec

  implicit lazy val signUpResponseCodec: Codec[SignUpResponse] = deriveCodec

}

object AuthenticationCodecs extends AuthenticationCodecs

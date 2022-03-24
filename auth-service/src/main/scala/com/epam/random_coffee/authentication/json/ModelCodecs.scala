package com.epam.random_coffee.authentication.json

import com.epam.random_coffee.authentication.model.{ AccessTokenContent, Email, UserId }
import io.circe.generic.semiauto.deriveCodec
import io.circe.{ Codec, Decoder, Encoder }

trait ModelCodecs {

  implicit lazy val emailCodec: Codec[Email] =
    Codec.from(Decoder.decodeString.map(Email), Encoder.encodeString.contramap(_.value))

  implicit lazy val userIdCodec: Codec[UserId] =
    Codec.from(Decoder.decodeString.map(UserId), Encoder.encodeString.contramap(_.value))

  implicit lazy val accessTokenContentCodec: Codec[AccessTokenContent] = deriveCodec

}

object ModelCodecs extends ModelCodecs

package com.epam.random_coffee.events.json

import com.epam.random_coffee.events.model.{ EventId, UserId }
import io.circe.{ Codec, Decoder, Encoder }

trait ModelCodecs {
  implicit lazy val eventIdCodec: Codec[EventId] =
    Codec.from(Decoder.decodeString.map(EventId.apply), Encoder.encodeString.contramap(_.value))

  implicit lazy val authorCodec: Codec[UserId] =
    Codec.from(Decoder.decodeString.map(UserId), Encoder.encodeString.contramap(_.id))

}

object ModelCodecs extends ModelCodecs

package com.epam.random_coffee.events.json

import com.epam.random_coffee.events.model.EventId
import io.circe.{ Codec, Decoder, Encoder }

trait ModelCodecs {
  implicit lazy val eventIdCodec: Codec[EventId] =
    Codec.from(Decoder.decodeString.map(EventId.apply), Encoder.encodeString.contramap(_.value))
}

object ModelCodecs extends ModelCodecs

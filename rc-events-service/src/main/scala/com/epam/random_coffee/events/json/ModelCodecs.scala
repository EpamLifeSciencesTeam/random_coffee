package com.epam.random_coffee.events.json

import com.epam.random_coffee.events.model.{ Author, EventId }
import io.circe.{ Codec, Decoder, Encoder }

trait ModelCodecs {
  implicit lazy val eventIdCodec: Codec[EventId] =
    Codec.from(Decoder.decodeString.map(EventId.apply), Encoder.encodeString.contramap(_.value))

  implicit lazy val authorCodec: Codec[Author] =
    Codec.from(Decoder.decodeString.map(Author), Encoder.encodeString.contramap(_.id))

}

object ModelCodecs extends ModelCodecs

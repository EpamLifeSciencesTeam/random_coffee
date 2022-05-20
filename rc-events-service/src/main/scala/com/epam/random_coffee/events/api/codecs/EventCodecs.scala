package com.epam.random_coffee.events.api.codecs

import com.epam.random_coffee.events.api.request.{ CreateEventRequest, UpdateEventRequest }
import com.epam.random_coffee.events.api.response.EventView
import com.epam.random_coffee.events.json.ModelCodecs
import com.epam.random_coffee.events.model.{ Event, RandomCoffeeEvent }
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

trait EventCodecs extends ModelCodecs with FailFastCirceSupport {

  implicit lazy val CreateEventRequestCodec: Codec[CreateEventRequest] = deriveCodec

  implicit lazy val updateEventRequestCodec: Codec[UpdateEventRequest] = deriveCodec

  implicit lazy val eventCodec: Codec[Event] = deriveCodec

  implicit lazy val eventViewCodec: Codec[EventView] = deriveCodec

  implicit lazy val randomCoffeeEventCodec: Codec[RandomCoffeeEvent] = deriveCodec
}

object EventCodecs extends EventCodecs

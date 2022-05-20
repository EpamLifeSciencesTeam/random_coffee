package com.epam.random_coffee.events.model

import java.time.Instant

final case class EventId(value: String) extends AnyVal

final case class Event(id: EventId, name: String)

final case class Author(id: String) extends AnyVal

final case class DateForEvent(date: Instant) extends AnyVal

final case class RandomCoffeeEvent(
  id: EventId,
  eventName: String,
  description: String,
  eventDate: DateForEvent,
  creationDate: DateForEvent,
  author: Author
)

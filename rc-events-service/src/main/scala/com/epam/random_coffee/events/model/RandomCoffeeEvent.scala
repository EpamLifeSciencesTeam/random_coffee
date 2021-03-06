package com.epam.random_coffee.events.model

import java.time.Instant

final case class EventId(value: String) extends AnyVal

final case class RandomCoffeeEvent(
  id: EventId,
  name: String,
  description: String,
  eventDate: Instant,
  createdAt: Instant,
  author: UserId
)

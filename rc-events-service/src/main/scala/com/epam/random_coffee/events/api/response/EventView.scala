package com.epam.random_coffee.events.api.response

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent, UserId }

import java.time.Instant

case class EventView(
  id: EventId,
  name: String,
  description: String,
  eventDate: Instant,
  author: UserId
)

object EventView {
  def fromEvent(event: RandomCoffeeEvent): EventView =
    EventView(event.id, event.name, event.description, event.eventDate, event.author)
}

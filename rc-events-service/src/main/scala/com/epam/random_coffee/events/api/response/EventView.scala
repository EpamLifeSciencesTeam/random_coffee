package com.epam.random_coffee.events.api.response

import com.epam.random_coffee.events.model.{ Author, DummyEvent, EventId, RandomCoffeeEvent }

import java.time.Instant

case class EventView(id: EventId, name: String)

case class RCEventView(
  id: EventId,
  name: String,
  description: String,
  eventDate: Instant,
  createdAt: Instant,
  author: Author
)

object EventView {
  def fromEvent(event: DummyEvent): EventView = EventView(event.id, event.name)
}
object RCEventView {
  def fromRCEvent(rCEvent: RandomCoffeeEvent): RCEventView =
    RCEventView(rCEvent.id, rCEvent.name, rCEvent.description, rCEvent.eventDate, rCEvent.createdAt, rCEvent.author)
}

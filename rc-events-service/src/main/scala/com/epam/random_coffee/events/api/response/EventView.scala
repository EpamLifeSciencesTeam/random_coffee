package com.epam.random_coffee.events.api.response

import com.epam.random_coffee.events.model.{ Event, EventId }

case class EventView(id: EventId, name: String)

object EventView {
  def fromEvent(event: Event): EventView = EventView(event.id, event.name)
}

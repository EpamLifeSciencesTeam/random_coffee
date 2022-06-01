package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ DummyEvent, EventId, RandomCoffeeEvent }

import java.time.Instant
import scala.concurrent.Future

trait EventService {

  def create(
    name: String,
    description: String,
    eventDate: Instant,
    author: String
  ): Future[RandomCoffeeEvent]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(id: EventId, newEventName: String): Future[DummyEvent]

  def delete(id: EventId): Future[Unit]

}

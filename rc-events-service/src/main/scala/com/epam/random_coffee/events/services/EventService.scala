package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ Event, EventId, RandomCoffeeEvent }

import scala.concurrent.Future

trait EventService {

  def create(
    name: String,
    description: String,
    eventDate: String,
    author: String
  ): Future[RandomCoffeeEvent]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(id: EventId, newEventName: String): Future[Event]

  def delete(id: EventId): Future[Unit]

}

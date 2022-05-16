package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ Event, EventId }

import scala.concurrent.Future

trait EventService {

  def create(eventName: String): Future[Event]

  def get(id: EventId): Future[Option[Event]]

  def update(id: EventId, newEventName: String): Future[Event]

  def delete(id: EventId): Future[Unit]
}

package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent, UserId }

import java.time.Instant
import scala.concurrent.Future

trait EventService {

  def create(
    name: String,
    description: String,
    eventDate: Instant,
    author: UserId
  ): Future[RandomCoffeeEvent]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(
    id: EventId,
    newName: Option[String],
    newDescription: Option[String],
    newEventDate: Option[Instant]
  ): Future[RandomCoffeeEvent]

  def delete(id: EventId): Future[Unit]

}

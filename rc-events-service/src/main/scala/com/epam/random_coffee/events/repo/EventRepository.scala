package com.epam.random_coffee.events.repo

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent }

import java.time.Instant
import scala.concurrent.Future

trait EventRepository {
  def save(rcEvent: RandomCoffeeEvent): Future[Unit]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(id: EventId, newName: String, newDescription: String, newEventDate: Instant): Future[Unit]

  def delete(id: EventId): Future[Unit]
}

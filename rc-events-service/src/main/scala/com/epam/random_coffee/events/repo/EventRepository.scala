package com.epam.random_coffee.events.repo

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent }

import scala.concurrent.Future

trait EventRepository {
  def save(event: RandomCoffeeEvent): Future[Unit]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(updatedEvent: RandomCoffeeEvent): Future[Unit]

  def delete(id: EventId): Future[Unit]
}

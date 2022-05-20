package com.epam.random_coffee.events.repo

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent }

import scala.concurrent.Future

trait EventRepository {
  def save(rcEvent: RandomCoffeeEvent): Future[Unit]

  def get(id: EventId): Future[Option[RandomCoffeeEvent]]

  def update(id: EventId, newName: String): Future[Unit]

  def delete(id: EventId): Future[Unit]
}

package com.epam.random_coffee.events.services.impl

import com.epam.random_coffee.events.model.{ Author, DummyEvent, EventId, RandomCoffeeEvent }
import com.epam.random_coffee.events.repo.EventRepository
import com.epam.random_coffee.events.services.EventService

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }

class EventServiceImpl(repo: EventRepository)(implicit ec: ExecutionContext) extends EventService {

  override def create(
    name: String,
    description: String,
    eventDate: Instant,
    author: String
  ): Future[RandomCoffeeEvent] = {
    val id = EventId(UUID.randomUUID().toString)
    val createdAt = Instant.now().truncatedTo(ChronoUnit.MINUTES)
    val authorEvent = Author(author)
    val rcEvent = RandomCoffeeEvent(id, name, description, eventDate, createdAt, authorEvent)
    repo.save(rcEvent).map(_ => rcEvent)
  }

  override def get(id: EventId): Future[Option[RandomCoffeeEvent]] = repo.get(id)

  override def delete(id: EventId): Future[Unit] =
    for {
      existingEvent <- repo.get(id)
      nothing <- existingEvent.fold(notFoundError(id))(_ => Future.unit)
      _ <- repo.delete(id)
    } yield nothing

  override def update(id: EventId, newEventName: String): Future[DummyEvent] =
    for {
      existingEvent <- repo.get(id)
      _ <- existingEvent.fold(notFoundError(id))(_ => Future.unit)
      _ <- repo.update(id, newEventName)
      event = DummyEvent(id, newEventName)
    } yield event

  private def notFoundError(id: EventId): Future[Unit] =
    Future.failed(new IllegalArgumentException(s"Event with id ${id.value} doesn't exist"))
}

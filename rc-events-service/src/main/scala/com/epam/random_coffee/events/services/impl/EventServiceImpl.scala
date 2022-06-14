package com.epam.random_coffee.events.services.impl

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent, UserId }
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
    author: UserId
  ): Future[RandomCoffeeEvent] = {
    val id = EventId(UUID.randomUUID().toString)
    val createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS)
    val rcEvent = RandomCoffeeEvent(id, name, description, eventDate, createdAt, author)
    repo.save(rcEvent).map(_ => rcEvent)
  }

  override def get(id: EventId): Future[Option[RandomCoffeeEvent]] = repo.get(id)

  override def delete(id: EventId): Future[Unit] =
    for {
      existingEvent <- get(id)
      _ <- existingEvent.fold(notFoundError(id))(Future.successful)
      nothing <- repo.delete(id)
    } yield nothing

  override def update(
    id: EventId,
    newName: Option[String],
    newDescription: Option[String],
    newEventDate: Option[Instant]
  ): Future[RandomCoffeeEvent] =
    for {
      existingEvent <- get(id)
      initialEvent <- existingEvent.fold(notFoundError(id))(Future.successful)
      name = newName.getOrElse(initialEvent.name)
      description = newDescription.getOrElse(initialEvent.description)
      dateOfEvent = newEventDate.getOrElse(initialEvent.eventDate)
      updatedEvent = RandomCoffeeEvent(
        id,
        name,
        description,
        dateOfEvent,
        initialEvent.createdAt,
        initialEvent.author
      )
      _ <- repo.update(updatedEvent)
    } yield updatedEvent

  private def notFoundError(id: EventId): Future[RandomCoffeeEvent] =
    Future.failed(new IllegalArgumentException(s"Event with id ${id.value} doesn't exist"))
}

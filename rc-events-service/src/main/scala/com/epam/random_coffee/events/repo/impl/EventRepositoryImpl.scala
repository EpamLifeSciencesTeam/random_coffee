package com.epam.random_coffee.events.repo.impl

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.events.model.{ DateForEvent, EventId, RandomCoffeeEvent }
import com.epam.random_coffee.events.repo.EventRepository
import doobie.{ Get, Put, Transactor }
import doobie.implicits._
import doobie.postgres.implicits._

import java.time.Instant
import scala.concurrent.Future

class EventRepositoryImpl(transactor: Transactor[IO])(implicit runtime: IORuntime) extends EventRepository {

  override def save(rcEvent: RandomCoffeeEvent): Future[Unit] = unsafeRun {
    sql"""insert into event (id, name, description, event_date, creation_date, author) 
    values (${rcEvent.id}, ${rcEvent.eventName}, ${rcEvent.description},
     ${rcEvent.eventDate}, ${rcEvent.creationDate}, ${rcEvent.author})""".update.run.map(_ => ())
  }

  override def get(id: EventId): Future[Option[RandomCoffeeEvent]] = unsafeRun {
    sql"select id, name, description, event_date, creation_date, author from event where id = ${id.value}"
      .query[RandomCoffeeEvent]
      .option
  }

  override def update(id: EventId, newName: String): Future[Unit] = unsafeRun {
    sql"update event set name = $newName where id = ${id.value}".update.run.map(_ => ())
  }

  override def delete(id: EventId): Future[Unit] = unsafeRun {
    sql"delete from event where id = ${id.value}".update.run.map(_ => ())
  }

  implicit lazy val datePut: Put[DateForEvent] = Put[Instant].contramap(_.date)

  implicit lazy val dateGet: Get[DateForEvent] = Get[Instant].map(DateForEvent)

  private def unsafeRun[T](query: doobie.ConnectionIO[T]): Future[T] =
    query.transact(transactor).unsafeToFuture()

}

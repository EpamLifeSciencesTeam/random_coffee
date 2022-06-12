package com.epam.random_coffee.events.repo.impl

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent }
import com.epam.random_coffee.events.repo.EventRepository
import doobie.Transactor
import doobie.implicits._
import doobie.postgres.implicits._

import scala.concurrent.Future

class EventRepositoryImpl(transactor: Transactor[IO])(implicit runtime: IORuntime) extends EventRepository {

  override def save(event: RandomCoffeeEvent): Future[Unit] = unsafeRun {
    sql"""insert into event (id, name, description, event_date, create_at, author) 
    values (${event.id}, ${event.name}, ${event.description},
     ${event.eventDate}, ${event.createdAt}, ${event.author})""".update.run.map(_ => ())
  }

  override def get(id: EventId): Future[Option[RandomCoffeeEvent]] = unsafeRun {
    sql"select id, name, description, event_date, create_at, author from event where id = ${id.value}"
      .query[RandomCoffeeEvent]
      .option
  }

  override def update(updatedEvent: RandomCoffeeEvent): Future[Unit] =
    unsafeRun {
      sql"""update event set name = ${updatedEvent.name}, description = ${updatedEvent.description},
           event_date = ${updatedEvent.eventDate}, create_at = ${updatedEvent.createdAt}, 
           author = ${updatedEvent.author} where id = ${updatedEvent.id}""".update.run.map(_ => ())
    }

  override def delete(id: EventId): Future[Unit] = unsafeRun {
    sql"delete from event where id = ${id.value}".update.run.map(_ => ())
  }

  private def unsafeRun[T](query: doobie.ConnectionIO[T]): Future[T] =
    query.transact(transactor).unsafeToFuture()

}

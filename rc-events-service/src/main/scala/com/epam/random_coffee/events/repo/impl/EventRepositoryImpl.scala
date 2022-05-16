package com.epam.random_coffee.events.repo.impl

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.events.model.{ Event, EventId }
import com.epam.random_coffee.events.repo.EventRepository
import doobie.Transactor
import doobie.implicits._

import scala.concurrent.Future

class EventRepositoryImpl(transactor: Transactor[IO])(implicit runtime: IORuntime) extends EventRepository {

  override def save(event: Event): Future[Unit] = unsafeRun {
    sql"insert into event (id, name) values (${event.id.value}, ${event.name})".update.run.map(_ => ())
  }

  override def get(id: EventId): Future[Option[Event]] = unsafeRun {
    sql"select id, name from event where id = ${id.value}".query[Event].option
  }

  override def update(id: EventId, newName: String): Future[Unit] = unsafeRun {
    sql"update event set name = $newName where id = ${id.value}".update.run.map(_ => ())
  }

  override def delete(id: EventId): Future[Unit] = unsafeRun {
    sql"delete from event where id = ${id.value}".update.run.map(_ => ())
  }

  private def unsafeRun[T](query: doobie.ConnectionIO[T]): Future[T] =
    query.transact(transactor).unsafeToFuture()

}

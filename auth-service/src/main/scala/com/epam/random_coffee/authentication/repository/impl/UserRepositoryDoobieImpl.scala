package com.epam.random_coffee.authentication.repository.impl

import cats.effect._
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.authentication.model.{ Email, User }
import com.epam.random_coffee.authentication.repository.UserRepository
import doobie._
import doobie.implicits._

import scala.concurrent.Future

class UserRepositoryDoobieImpl(transactor: Transactor[IO])(implicit runtime: IORuntime)
    extends UserRepository
    with DoobieSupport {

  override def save(user: User): Future[Unit] = unsafeRun {
    sql"""insert into "user" (id, email, name, password) values (${user.id}, ${user.email}, ${user.name}, ${user.password})""".update.run
      .map(_ => ())
  }

  override def find(email: Email): Future[Option[User]] =
    unsafeRun {
      sql"""select id, email, name, password from "user" where email = $email""".query[User].option
    }

  private def unsafeRun[T](query: doobie.ConnectionIO[T]): Future[T] =
    query.transact(transactor).unsafeToFuture()

}

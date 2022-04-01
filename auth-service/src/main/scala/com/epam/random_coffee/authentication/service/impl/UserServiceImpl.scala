package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.model.{ Email, Password, User, UserId }
import com.epam.random_coffee.authentication.repository.UserRepository
import com.epam.random_coffee.authentication.service.UserService

import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }

class UserServiceImpl(repo: UserRepository)(implicit ec: ExecutionContext) extends UserService {

  override def create(email: Email, name: String, password: Password): Future[User] =
    for {
      existingUser <- find(email)
      _ <- existingUser.fold(Future.unit)(_ => duplicateUserErr(email))
      id = UserId(UUID.randomUUID().toString)
      user = User(id, email, name, password)
      _ <- save(user)
    } yield user

  override def find(email: Email): Future[Option[User]] = repo.find(email)

  private def save(user: User): Future[Unit] = repo.save(user)

  private def duplicateUserErr(email: Email): Future[Unit] =
    Future.failed(new IllegalArgumentException(s"User with email ${email.value} already exists"))

}

package com.epam.random_coffee.authentication.repository

import com.epam.random_coffee.authentication.model.{ Email, User }

import scala.concurrent.Future

trait UserRepository {

  def save(user: User): Future[Unit]

  def find(email: Email): Future[Option[User]]

}

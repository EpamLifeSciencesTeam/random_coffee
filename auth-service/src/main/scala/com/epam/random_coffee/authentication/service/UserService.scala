package com.epam.random_coffee.authentication.service

import com.epam.random_coffee.authentication.model.{ Email, Password, User }

import scala.concurrent.Future

trait UserService {

  def create(email: Email, name: String, password: Password): Future[User]

  def find(email: Email): Future[Option[User]]

}

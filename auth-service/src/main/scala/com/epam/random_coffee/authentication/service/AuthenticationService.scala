package com.epam.random_coffee.authentication.service

import com.epam.random_coffee.authentication.model.{ AccessToken, Email, UserId }

import scala.concurrent.Future

trait AuthenticationService {

  def signIn(email: Email, password: String): Future[AccessToken]

  def signUp(email: Email, name: String, password: String): Future[UserId]

}

package com.epam.random_coffee.authentication.model

final case class User(id: UserId, email: Email, name: String, password: Password)

final case class UserId(value: String) extends AnyVal

final case class Email(value: String) extends AnyVal

final case class Password(hash: String, salt: String)

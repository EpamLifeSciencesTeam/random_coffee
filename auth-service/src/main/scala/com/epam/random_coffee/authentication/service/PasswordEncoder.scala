package com.epam.random_coffee.authentication.service

trait PasswordEncoder {

  def encode(rawPassword: String, salt: String): String

}

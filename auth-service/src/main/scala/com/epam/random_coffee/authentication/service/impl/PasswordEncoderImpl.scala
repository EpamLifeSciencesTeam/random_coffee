package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.service.PasswordEncoder

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class PasswordEncoderImpl() extends PasswordEncoder {

  private val encoder = MessageDigest.getInstance("SHA-256")

  override def encode(rawPassword: String, salt: String): String = synchronized {
    val hash = new BigInteger(1, encoder.digest((rawPassword + salt).getBytes(StandardCharsets.UTF_8)))
    String.format("%032x", hash)
  }

}

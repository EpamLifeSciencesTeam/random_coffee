package com.epam.random_coffee.authentication.repository.impl

import com.epam.random_coffee.authentication.model.{ Email, Password, UserId }
import doobie.Meta

trait DoobieSupport {

  implicit lazy val userIdMeta: Meta[UserId] = Meta.StringMeta.imap(UserId)(_.value)

  implicit lazy val emailMeta: Meta[Email] = Meta.StringMeta.imap(Email)(_.value)

  implicit lazy val passwordMeta: Meta[Password] = {
    val put: Password => String = pwd => s"${pwd.salt}:${pwd.hash}"
    val get: String => Password = pwd => {
      val (salt, _hash) = pwd.span(_ != ':')
      Password(hash = _hash.tail, salt = salt)
    }
    Meta.StringMeta.imap(get)(put)
  }

}

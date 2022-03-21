package com.epam.random_coffee.authentication.api.response

import com.epam.random_coffee.authentication.model.{ Email, UserId }

final case class SignUpResponse(id: UserId, email: Email, name: String)

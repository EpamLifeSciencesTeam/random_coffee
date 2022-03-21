package com.epam.random_coffee.authentication.model

final case class AccessToken(value: String)

final case class AccessTokenContent(userId: UserId, email: Email)

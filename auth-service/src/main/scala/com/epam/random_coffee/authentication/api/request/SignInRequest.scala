package com.epam.random_coffee.authentication.api.request

import com.epam.random_coffee.authentication.model.Email

final case class SignInRequest(email: Email, password: String)

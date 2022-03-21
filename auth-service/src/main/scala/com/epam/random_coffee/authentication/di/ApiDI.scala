package com.epam.random_coffee.authentication.di

import com.epam.random_coffee.authentication.api.AuthenticationAPI

class ApiDI(serviceDI: ServiceDI) {

  import serviceDI._

  lazy val authenticationAPI: AuthenticationAPI = new AuthenticationAPI(authenticationService)

}

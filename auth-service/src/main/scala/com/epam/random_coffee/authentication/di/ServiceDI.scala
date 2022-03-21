package com.epam.random_coffee.authentication.di

import com.epam.random_coffee.authentication.config.TokensConfig
import com.epam.random_coffee.authentication.service._
import com.epam.random_coffee.authentication.service.impl._

import scala.concurrent.ExecutionContext

class ServiceDI(tokensConfig: TokensConfig, repositoryDI: RepositoryDI)(implicit ec: ExecutionContext) {

  import repositoryDI._

  lazy val passwordEncoder: PasswordEncoder = new PasswordEncoderImpl()

  lazy val userService: UserService = new UserServiceImpl(userRepository)

  lazy val authenticationService: AuthenticationService =
    new AuthenticationServiceImpl(userService, passwordEncoder, tokensConfig)

}

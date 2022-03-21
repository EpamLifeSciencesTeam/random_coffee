package com.epam.random_coffee.authentication.config

import pdi.jwt.algorithms.JwtHmacAlgorithm

import scala.concurrent.duration.Duration

final case class AuthenticationServiceConfig(db: DbConfig, liquibase: LiquibaseConfig, tokens: TokensConfig)

final case class DbConfig(driver: String, url: String, user: String, password: String)

final case class LiquibaseConfig(changelogPath: String)

final case class TokensConfig(secretKey: String, hmacAlgorithm: JwtHmacAlgorithm, accessTokenTtl: Duration)

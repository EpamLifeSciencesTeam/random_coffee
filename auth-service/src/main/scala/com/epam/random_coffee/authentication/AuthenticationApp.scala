package com.epam.random_coffee.authentication

import akka.http.scaladsl.server.Route
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.authentication.config.AuthenticationServiceConfig
import com.epam.random_coffee.authentication.di.{ ApiDI, RepositoryDI, ServiceDI }

import scala.concurrent.ExecutionContext
import scala.util.Try

class AuthenticationApp(config: AuthenticationServiceConfig) {

  def init: Try[Unit] = repositoryDI.liquibaseMigrator.runMigrations(config.liquibase.changelogPath)

  def api: Try[Route] = Try(apiDI.authenticationAPI.routes)

  private implicit val runtime: IORuntime = IORuntime.global

  private implicit val ec: ExecutionContext = runtime.compute

  private val repositoryDI = new RepositoryDI(config.db)

  private val serviceDI = new ServiceDI(config.tokens, repositoryDI)

  private val apiDI = new ApiDI(serviceDI)

}

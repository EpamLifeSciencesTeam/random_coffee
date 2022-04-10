package com.epam.random_coffee.events

import akka.http.scaladsl.server.Route
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.events.config.EventServiceConfig
import com.epam.random_coffee.events.di.{ ApiDI, RepositoryDI, ServiceDI }

import scala.concurrent.ExecutionContext
import scala.util.Try

class EventApp(config: EventServiceConfig) {
  def init: Try[Unit] = repositoryDI.liquibaseMigrator.runMigrations(config.liquibase.changelogPath)

  def api: Try[Route] = Try(apiDI.rcEventsApi.routes)

  private implicit val runtime: IORuntime = IORuntime.global

  private implicit val ec: ExecutionContext = runtime.compute

  private val repositoryDI = new RepositoryDI(config.db)

  private val serviceDI = new ServiceDI(repositoryDI)

  private val apiDI = new ApiDI(serviceDI)
}

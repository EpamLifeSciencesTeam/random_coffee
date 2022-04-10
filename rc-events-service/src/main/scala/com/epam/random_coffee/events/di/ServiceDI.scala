package com.epam.random_coffee.events.di

import com.epam.random_coffee.events.services.EventService
import com.epam.random_coffee.events.services.impl.EventServiceImpl

import scala.concurrent.ExecutionContext

class ServiceDI(repositoryDI: RepositoryDI)(implicit ec: ExecutionContext) {

  import repositoryDI._

  lazy val eventService: EventService = new EventServiceImpl(eventRepository)

}

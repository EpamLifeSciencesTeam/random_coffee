package com.epam.random_coffee.events.di

import com.epam.random_coffee.events.api.RcEventsAPI

import scala.concurrent.ExecutionContext

class ApiDI(serviceDI: ServiceDI)(implicit ec: ExecutionContext) {

  lazy val rcEventsApi: RcEventsAPI = new RcEventsAPI(serviceDI.eventService)

}

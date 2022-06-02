package com.epam.random_coffee.events.api.request

import java.time.Instant

case class UpdateEventRequest(
  name: Option[String],
  description: Option[String],
  eventDate: Option[Instant]
)

package com.epam.random_coffee.events.api.request

import java.time.Instant

case class CreateEventRequest(name: String, description: String, eventDate: Instant, author: String)

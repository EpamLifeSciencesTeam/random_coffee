package com.epam.random_coffee.events.api.request

case class CreateEventRequest(eventName: String, description: String, eventDate: String, author: String)

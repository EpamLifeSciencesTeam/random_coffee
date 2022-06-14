package com.epam.random_coffee.events.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ PathMatcher, PathMatcher1, Route }
import com.epam.random_coffee.events.api.codecs.EventCodecs
import com.epam.random_coffee.events.api.request.{ CreateEventRequest, UpdateEventRequest }
import com.epam.random_coffee.events.api.response.EventView
import com.epam.random_coffee.events.model.EventId
import com.epam.random_coffee.events.services.EventService

import scala.concurrent.ExecutionContext

class RcEventsAPI(eventService: EventService)(implicit ec: ExecutionContext) extends EventCodecs {

  def routes: Route = pathPrefix("events" / "v1")(createEvent ~ deleteEvent ~ getEvent ~ updateEvent)

  val eventIdMatcher: PathMatcher1[EventId] = PathMatcher(Segment).map(EventId)

  private lazy val createEvent: Route =
    (post & entity(as[CreateEventRequest]))(
      request =>
        complete(
          eventService
            .create(request.name, request.description, request.eventDate, request.author)
            .map(event => EventView.fromEvent(event))
        )
    )

  private lazy val deleteEvent: Route =
    (path(eventIdMatcher) & delete)(id => complete(eventService.delete(id)))

  private lazy val getEvent: Route =
    (path(eventIdMatcher) & get)(
      id =>
        rejectEmptyResponse(complete {
          eventService.get(id).map(eventOpt => eventOpt.map(event => EventView.fromEvent(event)))
        })
    )

  private lazy val updateEvent: Route =
    (path(eventIdMatcher) & put & entity(as[UpdateEventRequest])) { (id, event) =>
      complete(
        eventService.update(id, event.name, event.description, event.eventDate).map(event => EventView.fromEvent(event))
      )
    }
}

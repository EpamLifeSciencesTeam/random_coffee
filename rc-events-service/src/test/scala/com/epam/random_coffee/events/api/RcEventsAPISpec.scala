package com.epam.random_coffee.events.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.epam.random_coffee.events.api.request.{ CreateEventRequest, UpdateEventRequest }
import com.epam.random_coffee.events.model.{ Author, DateForEvent, Event, EventId, RandomCoffeeEvent }
import com.epam.random_coffee.events.services.EventService
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.wordspec.AnyWordSpec
import com.epam.random_coffee.events.api.codecs.EventCodecs._

import java.time.Instant
import scala.concurrent.Future

class RcEventsAPISpec extends AnyWordSpec with MockFactory with OneInstancePerTest with ScalatestRouteTest {

  private val eventService = mock[EventService]
  private val eventAPI = new RcEventsAPI(eventService)
  private val routes = Route.seal(eventAPI.routes)
  private val id = EventId("uuid_test")
  private val author = Author("author_Id")
  private val eventInstant = Instant.parse("2020-01-21T20:00:00Z")
  private val eventDate = DateForEvent(eventInstant)
  private val creationDate = DateForEvent(eventInstant.minusSeconds(60))

  private val rCEvent =
    RandomCoffeeEvent(id, "create", "description", eventDate, creationDate, author)

  private val updatedEvent = Event(id, "updated_event")

  private val createEventRequest =
    CreateEventRequest("create", "description", "2020-01-21T20:00:00Z", "author_Id")

  private val updateEventRequest = UpdateEventRequest("updated_event")

  "RcEventsAPI" should {
    "return a newly created event" when {
      "user create event" in {
        (eventService.create _)
          .expects("create", "description", "2020-01-21T20:00:00Z", "author_Id")
          .returns(Future.successful(rCEvent))

        Post("/events/v1", createEventRequest) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(entityAs[RandomCoffeeEvent] == rCEvent)
        }
      }
    }

    "find existed event" when {
      "user search event" in {
        (eventService.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        Get("/events/v1/uuid_test") ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(entityAs[Option[RandomCoffeeEvent]].contains(rCEvent))
        }
      }
    }

    "delete existed event" when {
      "user delete event" in {
        (eventService.delete _).expects(id).returns(Future.unit)

        Delete("/events/v1/uuid_test") ~> routes ~> check {
          assert(status == StatusCodes.OK)
        }
      }
    }

    "update existed event" when {
      "user update event" in {
        (eventService.update _).expects(id, updateEventRequest.name).returns(Future.successful(updatedEvent))

        Put("/events/v1/uuid_test", updateEventRequest) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(entityAs[Event] == updatedEvent)
        }
      }
    }

    "fail with 500" when {

      "user's attempt to delete a non-existent event" in {
        (eventService.delete _).expects(*).returns(Future.failed(new RuntimeException))

        Delete("/events/v1/uuid_test") ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }

      "user's attempt to update a non-existent event" in {
        (eventService.update _).expects(*, *).returns(Future.failed(new RuntimeException))

        Put("/events/v1/uuid_test", updateEventRequest) ~> routes ~> check {
          assert(status == StatusCodes.InternalServerError)
        }
      }
    }

    "fail with 404" when {

      "event not found" in {
        (eventService.get _).expects(id).returns(Future.successful(None))

        Get("/events/v1/uuid_test") ~> routes ~> check {
          assert(status == StatusCodes.NotFound)
        }
      }
    }
  }
}

package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ EventId, RandomCoffeeEvent, UserId }
import com.epam.random_coffee.events.repo.EventRepository
import com.epam.random_coffee.events.services.impl.EventServiceImpl
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.wordspec.AsyncWordSpec

import java.time.Instant
import scala.concurrent.Future

class EventServiceImplSpec extends AsyncWordSpec with AsyncMockFactory with OneInstancePerTest {
  private val repo = mock[EventRepository]
  private val service = new EventServiceImpl(repo)

  private val id = EventId("uuid_test")
  private val author = UserId("author_Id")

  private val eventDate = Instant.parse("2020-01-21T20:00:00Z")

  private val creationDate = eventDate.plusSeconds(60)

  private val rCEvent =
    RandomCoffeeEvent(id, "create", "description", eventDate, creationDate, author)

  private val updatedName = "updatedName"
  private val updatedDescription = "updatedDescription"
  private val updatedEventDate = eventDate.plusSeconds(360)

  private val rCEventWithNewName =
    rCEvent.copy(name = updatedName)

  private val rCEventWithNewDescription =
    rCEvent.copy(description = updatedDescription)

  private val rCEventWithNewEventDate =
    rCEvent.copy(eventDate = updatedEventDate)

  private val rCEventFullyUpdated =
    rCEvent.copy(name = updatedName, description = updatedDescription, eventDate = updatedEventDate)

  "EventService" should {

    "create an event" when {
      "event doesn't exist" in {
        (repo.save _).expects(*).returns(Future.unit)

        service.create(rCEvent.name, rCEvent.description, eventDate, author).map(_ => succeed)
      }
    }

    "delete an event" when {
      "event exists" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.delete _).expects(*).returns(Future.unit)

        service.delete(id).map(_ => succeed)
      }
    }

    "update an event" when {
      "event exists and request contains all fields" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.update _).expects(rCEventFullyUpdated).returns(Future.unit)

        service
          .update(id, Some(updatedName), Some(updatedDescription), Some(updatedEventDate))
          .map(event => assert(event == rCEventFullyUpdated))
      }

      "request contains only the name" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.update _).expects(rCEventWithNewName).returns(Future.unit)

        service.update(id, Some(updatedName), None, None).map(event => assert(event == rCEventWithNewName))
      }
      "request contains only a description" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.update _).expects(rCEventWithNewDescription).returns(Future.unit)

        service
          .update(id, None, Some(updatedDescription), None)
          .map(event => assert(event == rCEventWithNewDescription))
      }
      "request contains only the date of the event" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.update _).expects(rCEventWithNewEventDate).returns(Future.unit)

        service.update(id, None, None, Some(updatedEventDate)).map(event => assert(event == rCEventWithNewEventDate))
      }

      "request contains nothing" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        (repo.update _).expects(rCEvent).returns(Future.unit)

        service.update(id, None, None, None).map(event => assert(event == rCEvent))
      }
    }

    "find an event" when {
      "there is an event in repo" in {
        (repo.get _).expects(id).returns(Future.successful(Some(rCEvent)))

        service.get(id).map(testEvent => assert(testEvent.contains(rCEvent)))
      }
    }

    "fail to delete an event" when {

      "such event doesn't exists" in {
        (repo.get _).expects(id).returns(Future.successful(None))

        service
          .delete(id)
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == s"Event with id ${id.value} doesn't exist"))
      }
    }

    "fail to update an event" when {

      "such event doesn't exists" in {
        (repo.get _).expects(id).returns(Future.successful(None))

        service
          .update(id, Some(rCEvent.name), Some(rCEvent.description), Some(rCEvent.eventDate))
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == s"Event with id ${id.value} doesn't exist"))
      }
    }

    "fail to create an event" when {

      "saving to database has failed" in {
        (repo.save _)
          .expects(*)
          .returns(
            Future.failed(new IllegalArgumentException("saving to database has failed"))
          )

        service
          .create(rCEvent.name, rCEvent.description, eventDate, author)
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == "saving to database has failed"))

      }
    }
  }
}

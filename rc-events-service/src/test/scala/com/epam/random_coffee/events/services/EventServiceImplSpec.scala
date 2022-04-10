package com.epam.random_coffee.events.services

import com.epam.random_coffee.events.model.{ Event, EventId }
import com.epam.random_coffee.events.repo.EventRepository
import com.epam.random_coffee.events.services.impl.EventServiceImpl
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

class EventServiceImplSpec extends AsyncWordSpec with AsyncMockFactory with OneInstancePerTest {
  private val repo = mock[EventRepository]
  private val service = new EventServiceImpl(repo)

  private val id = EventId("uuid_test")
  private val event = Event(id, "created_event")

  "EventService" should {

    "create an event" when {
      "event doesn't exist" in {
        (repo.save _).expects(*).returns(Future.unit)

        service.create(event.name).map(_ => succeed)
      }
    }

    "delete an event" when {
      "event exists" in {
        (repo.get _).expects(id).returns(Future.successful(Some(event)))

        (repo.delete _).expects(*).returns(Future.unit)

        service.delete(id).map(_ => succeed)
      }
    }

    "update an event" when {
      "event exists" in {
        (repo.get _).expects(id).returns(Future.successful(Some(event)))

        (repo.update _).expects(*, *).returns(Future.unit)

        service.update(id, event.name).map(_ => succeed)
      }
    }

    "find an event" when {
      "there is an event in repo" in {
        (repo.get _).expects(id).returns(Future.successful(Some(event)))

        service.get(id).map(testEvent => assert(testEvent.contains(event)))
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
          .update(id, event.name)
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

        service.create(event.name).failed.map(_.getMessage).map(msg => assert(msg == "saving to database has failed"))

      }
    }
  }
}

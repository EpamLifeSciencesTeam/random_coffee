package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.model.{ Email, Password, User, UserId }
import com.epam.random_coffee.authentication.repository.UserRepository
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

class UserServiceImplSpec extends AsyncWordSpec with AsyncMockFactory with OneInstancePerTest {

  private val repo = mock[UserRepository]
  private val service = new UserServiceImpl(repo)

  private val user = User(UserId("test_id"), Email("test_email"), "test_name", Password("test_hash", "test_salt"))

  "UserService" should {

    "create a user" when {

      "user doesn't exist" in {
        (repo.find _).expects(user.email).returns(Future.successful(None))
        (repo.save _).expects(*).returns(Future.unit)

        service.create(user.email, user.name, user.password).map(_ => succeed)
      }

    }

    "find a user" when {

      "there is a user in repo" in {
        (repo.find _).expects(user.email).returns(Future.successful(Some(user)))

        service.find(user.email).map(testUser => assert(testUser.contains(user)))
      }

      "there is no user in repo" in {
        (repo.find _).expects(user.email).returns(Future.successful(None))

        service.find(user.email).map(testUser => assert(testUser.isEmpty))
      }

    }

    "fail to create a user" when {

      "user with such email already exists" in {
        (repo.find _).expects(user.email).returns(Future.successful(Some(user)))

        service
          .create(user.email, user.name, user.password)
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == s"User with email ${user.email.value} already exists"))
      }

    }

  }

}

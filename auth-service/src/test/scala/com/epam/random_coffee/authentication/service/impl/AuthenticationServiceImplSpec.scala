package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.config.TokensConfig
import com.epam.random_coffee.authentication.model.{ AccessTokenContent, Email, Password, User, UserId }
import com.epam.random_coffee.authentication.service.{ PasswordEncoder, UserService }
import io.circe.parser
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.OneInstancePerTest
import com.epam.random_coffee.authentication.model.JsonCodecs.accessTokenContentCodec
import org.scalatest.wordspec.AsyncWordSpec
import pdi.jwt.{ Jwt, JwtAlgorithm }

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class AuthenticationServiceImplSpec extends AsyncWordSpec with AsyncMockFactory with OneInstancePerTest {

  private val config = TokensConfig("secret", JwtAlgorithm.HS256, 1.minute)
  private val encoder: PasswordEncoder = (pwd, salt) => s"$pwd + $salt"
  private val userService = mock[UserService]
  private val authenticationService = new AuthenticationServiceImpl(userService, encoder, config)

  private val oldUserPassword = "test_old_password"
  private val newUserPassword = "test_new_password"

  private val oldUserSalt = "test_old_salt"
  private val newUserSalt = "test_new_salt"

  private val oldUserHash = encoder.encode(oldUserPassword, oldUserSalt)
  private val newUserHash = encoder.encode(newUserPassword, newUserSalt)

  private val newUser =
    User(UserId("test_new_id"), Email("test_new_email"), "test_new_name", Password(newUserHash, newUserSalt))

  private val oldUser =
    User(UserId("test_old_id"), Email("test_old_email"), "test_old_name", Password(oldUserHash, oldUserSalt))

  "AuthenticationService" should {

    "sign in a user" when {

      "user with such email exists and password is correct" in {
        (userService.find _).expects(oldUser.email).returns(Future.successful(Some(oldUser)))

        authenticationService
          .signIn(oldUser.email, oldUserPassword)
          .map(token => Jwt.decode(token.value, config.secretKey, Seq(config.hmacAlgorithm)))
          .flatMap(Future.fromTry)
          .map(token => parser.parse(token.content))
          .flatMap(toFuture)
          .map(_.as[AccessTokenContent])
          .flatMap(toFuture)
          .map(content => assert(content.userId == oldUser.id && content.email == oldUser.email))
      }

    }

    "sign up a user" when {

      "user with such email doesn't exist yet" in {
        (userService.create _).expects(newUser.email, newUser.name, *).returns(Future.successful(newUser))

        authenticationService.signUp(newUser.email, newUser.name, newUserPassword).map(_ => succeed)
      }

    }

    "fail to sign in a user" when {

      "user doesn't exists" in {
        (userService.find _).expects(newUser.email).returns(Future.successful(None))

        authenticationService
          .signIn(newUser.email, newUserPassword)
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == s"User with email ${newUser.email.value} not found"))
      }

      "password is incorrect" in {
        (userService.find _).expects(oldUser.email).returns(Future.successful(Some(oldUser)))

        authenticationService
          .signIn(oldUser.email, newUserPassword)
          .failed
          .map(_.getMessage)
          .map(msg => assert(msg == s"Incorrect password for user with email ${oldUser.email.value}"))
      }

    }

  }

  private def toFuture[A](either: Either[Throwable, A]): Future[A] = Future.fromTry(either.toTry)

}

package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.config.TokensConfig
import com.epam.random_coffee.authentication.json.ModelCodecs._
import com.epam.random_coffee.authentication.model._
import com.epam.random_coffee.authentication.service.{ AuthenticationService, PasswordEncoder, UserService }
import io.circe.syntax.EncoderOps
import pdi.jwt.{ Jwt, JwtClaim }

import java.time.Instant
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

class AuthenticationServiceImpl(userService: UserService, passwordEncoder: PasswordEncoder, tokensConfig: TokensConfig)(
  implicit ec: ExecutionContext
) extends AuthenticationService {

  override def signIn(email: Email, password: String): Future[AccessToken] =
    for {
      userOpt <- userService.find(email)
      user <- userOpt.fold(userNotFoundErr(email))(Future.successful)
      hashedPassword = passwordEncoder.encode(password, user.password.salt)
      _ <- if (hashedPassword != user.password.hash) incorrectPasswordErr(email) else Future.unit
    } yield createAccessToken(user.id, email)

  override def signUp(email: Email, name: String, password: String): Future[UserId] = {
    val salt = Random.nextLong().toHexString
    val hashedPassword = passwordEncoder.encode(password, salt)

    userService.create(email, name, Password(hashedPassword, salt)).map(_.id)
  }

  private def createAccessToken(userId: UserId, email: Email): AccessToken = {
    val content = AccessTokenContent(userId, email)
    val issuedAt = Instant.now().getEpochSecond

    val claims = JwtClaim(
      content = content.asJson.noSpaces,
      expiration = Some(issuedAt + tokensConfig.accessTokenTtl.toSeconds),
      issuedAt = Some(issuedAt)
    )

    AccessToken(Jwt.encode(claims, tokensConfig.secretKey, tokensConfig.hmacAlgorithm))
  }

  private def userNotFoundErr(email: Email): Future[User] =
    Future.failed(new IllegalArgumentException(s"User with email ${email.value} not found"))

  private def incorrectPasswordErr(email: Email): Future[Unit] =
    Future.failed(new IllegalArgumentException(s"Incorrect password for user with email ${email.value}"))

}

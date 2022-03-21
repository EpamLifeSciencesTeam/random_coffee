package com.epam.random_coffee.authentication.service.impl

import com.epam.random_coffee.authentication.service.PasswordEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import java.util.concurrent.Executors
import scala.concurrent.{ ExecutionContext, Future }

class PasswordEncoderImplSpec extends AsyncWordSpec with Matchers {

  private implicit val multiThreadedEc: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  private val encoder: PasswordEncoder = new PasswordEncoderImpl()
  private val encode: (String, String) => String = encoder.encode
  private val encodeF: (String, String) => Future[String] = (p, s) => Future(encode(p, s))

  private val amount = 1000

  "PasswordEncoder" should {
    "not suffer from race conditions" when {
      "pass + hash are the same all the time" in {
        val password = "password"
        val salt = "salt"

        val data = Seq.fill(amount)((password, salt))

        val expectedHash = encode(password, salt)

        Future.traverse(data)(encodeF.tupled).map { actualHashes =>
          actualHashes should contain only expectedHash
        }
      }

      "pass + hash vary" in {
        val password = (1 to amount).map(i => s"password_$i")
        val salt = (1 to amount).map(i => s"salt_$i")

        val data = password.zip(salt)

        val expectedHashes = data.map(encode.tupled)

        Future.traverse(data)(encodeF.tupled).map { actualHashes =>
          assert(actualHashes == expectedHashes)
        }
      }
    }
  }

}

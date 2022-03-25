package com.epam.random_coffee.authentication.di

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.epam.random_coffee.authentication.config.DbConfig
import com.epam.random_coffee.authentication.repository.impl.UserRepositoryDoobieImpl
import com.epam.random_coffee.authentication.repository.{ LiquibaseMigrator, UserRepository }
import com.zaxxer.hikari.HikariDataSource
import doobie.Transactor

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class RepositoryDI(db: DbConfig)(implicit runtime: IORuntime) {

  lazy val userRepository: UserRepository = new UserRepositoryDoobieImpl(transactor)

  lazy val liquibaseMigrator: LiquibaseMigrator = new LiquibaseMigrator(hikariDs.getConnection)

  private lazy val hikariDs = {
    val ds = new HikariDataSource()

    Class.forName(db.driver)
    ds.setJdbcUrl(db.url)
    ds.setUsername(db.user)
    ds.setPassword(db.password)

    ds
  }

  private lazy val connectEC = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(32))

  private lazy val transactor = Transactor.fromDataSource[IO](hikariDs, connectEC)

}

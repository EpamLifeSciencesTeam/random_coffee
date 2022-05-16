package com.epam.random_coffee.database

import com.typesafe.scalalogging.StrictLogging
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.{ Contexts, Liquibase }

import java.sql.Connection
import scala.util.{ Failure, Success, Try }

class LiquibaseMigrator(connection: Connection) extends StrictLogging {

  private val liquibaseConnection = new JdbcConnection(connection)
  private val database = DatabaseFactory.getInstance.findCorrectDatabaseImplementation(liquibaseConnection)

  def runMigrations(changelogPath: String): Try[Unit] =
    Try {
      new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database).update(new Contexts())
    } match {
      case f @ Failure(exception) =>
        logger.error(s"Failed to update DB.", exception)
        f
      case s @ Success(_) =>
        logger.info("Successfully updated DB!")
        s
    }

}

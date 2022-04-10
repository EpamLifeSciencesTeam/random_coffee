package com.epam.random_coffee.events.config

final case class EventServiceConfig(db: DbConfig, liquibase: LiquibaseConfig)

final case class DbConfig(driver: String, url: String, user: String, password: String)

final case class LiquibaseConfig(changelogPath: String)

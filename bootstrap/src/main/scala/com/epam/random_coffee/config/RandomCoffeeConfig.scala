package com.epam.random_coffee.config

import com.epam.random_coffee.authentication.config._
import com.epam.random_coffee.events.config.EventServiceConfig
import pdi.jwt.JwtAlgorithm
import pdi.jwt.algorithms.JwtHmacAlgorithm
import pureconfig.ConfigReader
import pureconfig.generic.auto.exportReader
import pureconfig.generic.semiauto.deriveReader

final case class RandomCoffeeConfig(
  bootstrap: BootstrapConfig,
  authentication: AuthenticationServiceConfig,
  eventService: EventServiceConfig
)

object RandomCoffeeConfig {

  implicit lazy val jwtHmacAlgorithmReader: ConfigReader[JwtHmacAlgorithm] =
    ConfigReader.fromStringOpt { str =>
      Option(JwtAlgorithm.fromString(str)).collect { case algorithm: JwtHmacAlgorithm => algorithm }
    }

  implicit lazy val randomCoffeeConfigReader: ConfigReader[RandomCoffeeConfig] = deriveReader[RandomCoffeeConfig]

}

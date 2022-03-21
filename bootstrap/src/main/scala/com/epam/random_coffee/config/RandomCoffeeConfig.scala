package com.epam.random_coffee.config

import com.epam.random_coffee.authentication.config._
import pdi.jwt.JwtAlgorithm
import pdi.jwt.algorithms.JwtHmacAlgorithm
import pureconfig.ConfigReader
import pureconfig.generic.auto.exportReader
import pureconfig.generic.semiauto.deriveReader

final case class RandomCoffeeConfig(authentication: AuthenticationServiceConfig)

object RandomCoffeeConfig {

  implicit lazy val jwtHmacAlgorithmReader: ConfigReader[JwtHmacAlgorithm] =
    ConfigReader.fromStringOpt { str =>
      Option(JwtAlgorithm.fromString(str)).collect { case algorithm: JwtHmacAlgorithm => algorithm }
    }

  implicit lazy val randomCoffeeConfigReader: ConfigReader[RandomCoffeeConfig] = deriveReader[RandomCoffeeConfig]

}

Docker / defaultLinuxInstallLocation := sys.env.getOrElse("COFFEE_CONTAINER_PATH", "/random_coffee/app")
Docker / packageName := sys.env.getOrElse("COFFEE_CONTAINER_NAME", "random_coffee")
dockerExposedVolumes := Seq(sys.env.getOrElse("COFFEE_CONTAINER_LOGS", "/random_coffee/app/logs"))
dockerExposedPorts := Seq(sys.env.getOrElse("COFFEE_CONTAINER_PORT", "8080").toInt)

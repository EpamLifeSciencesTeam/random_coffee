# random_coffee

Dependencies:
- Java 11
- Scala 2.13.8
- SBT 1.6.2

# Getting Started

## Installation
### required dependencies installation
Install Java 11 JDK, Scala 2.13.8, the Scala build tool

## Compiling
To compile from the command line, use the command below:
```
    sbt compile
```

##  Running Locally
To run the application, use the following command:
```
    sbt bootstrap/run
```

## Running with docker compose
To run the application in docker, start by building application's docker image with:
```
  sbt clean docker:publishLocal 
```
After image is created, run:
```
docker compose up / docker compose up -d (if you wan't compose to run in detached mode)
```

### Running Unit Tests Locally
Use the command below to run unit tests:
```
    sbt test
```

## Build With
* [SBT](https://www.scala-sbt.org/) Build and dependency management
* [AkkaHTTP](https://doc.akka.io/docs/akka-http/current/index.html)  Asynchronous, streaming-first HTTP server and client
* [Doobie](https://tpolecat.github.io/doobie/) Database query and access library
* [Circe](https://circe.github.io/circe/) JSON processing
* [ScalaTest](http://www.scalatest.org/) Unit-testing framework

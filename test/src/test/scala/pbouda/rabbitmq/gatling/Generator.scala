package pbouda.rabbitmq.gatling

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef.{global, scenario, _}
import io.gatling.http.Predef.{http, status, _}

import scala.concurrent.duration.FiniteDuration

class Generator extends Simulation {

  private val httpConf = http.baseUrl("http://localhost:8080")

  private val invocation = scenario("Generate scenario")
    .exec(http("Generate")
      .get("/invoke")
      .check(status.is(200))
    )

  setUp(invocation.inject(
    rampUsersPerSec(1) to 600 during FiniteDuration(4, TimeUnit.MINUTES))
    .protocols(httpConf))
    .assertions(global.successfulRequests.percent.gte(100))
}

package template

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadTest extends Simulation {

  val feeder = csv("search.csv").random

  val httpProtocol = http
    .baseUrl("http://example.com")
    .acceptHeader("application/json")
    .connectionHeader("keep-alive")

  object Search1 {
    val search = feed(feeder)
      .exec(addCookie(Cookie("cookie", "${cookie}")))
      .exec(http("Search1")
        .get("/api/v1/search1")
        .queryParam("query", "${query}")
        .header("Keep-Alive", "150")
        .header("Content-Type", "application/json")
      )
  }

  object Search2 {
    val search = feed(feeder)
      .exec(addCookie(Cookie("cookie", "${cookie}")))
      .exec(http("Search2")
        .get("/api/v1/search2")
        .queryParam("query", "${query}")
        .header("Keep-Alive", "150")
        .header("Content-Type", "application/json")
      )
  }

  def run(): Unit = {
    val scenarioSearch1 = scenario("scenario Search1")
      .exec(Search1.search)
    val scenarioSearch2 = scenario("scenario Search2")
      .exec(Search2.search)

    val stepInjections = Seq(
      nothingFor(5 seconds),
      constantUsersPerSec(1) during (3 seconds),
      constantUsersPerSec(2) during (3 seconds),
      constantUsersPerSec(3) during (3 seconds),
      constantUsersPerSec(4) during (3 seconds),
      constantUsersPerSec(3) during (3 seconds),
      constantUsersPerSec(2) during (3 seconds),
      constantUsersPerSec(1) during (3 seconds)
    )

    val waveInjections = Seq(
      nothingFor(5 seconds),
      rampUsersPerSec(1) to 4 during (9 seconds),
      rampUsersPerSec(4) to 1 during (9 seconds)
    )

    /*
    setUp(
      scenarioSearch1.inject(stepInjections)
    )
    */
    setUp(
      // will be tested in parallel
      scenarioSearch1.inject(stepInjections),
      scenarioSearch2.inject(waveInjections)
    )
  }

  run()

}

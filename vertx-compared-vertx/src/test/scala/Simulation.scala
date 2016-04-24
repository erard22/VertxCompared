package Simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080/api/")
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scenarioAdd = scenario("AddUsers")
    .exec(http("add_request")
    .post("users")
    .body(StringBody("""{"name":"Erard", "firstname":"michel", "age":34}""")).asJSON)
    .pause(5)

  setUp(
    scenarioAdd.inject(constantUsersPerSec(100) during(30 seconds))).protocols(httpConf)
}
package lt.donatasmart.routing.akka

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import lt.donatasmart.routing.BaseSpec

trait RoutingSpec extends ScalatestRouteTest { self: BaseSpec =>

  def routing: CustomRouting
  implicit lazy val route: Route = routing.route

  def checkGet[T](endpoint: String)
                 (body: => T)
                 (implicit route: Route): T = Get(endpoint) ~> Route.seal(route) ~> check(body)

  def checkPost[T](endpoint: String)
                  (body: => T)
                  (implicit  route: Route): T = Post(endpoint) ~> Route.seal(route) ~> check(body)

  def checkDelete[T](endpoint: String)
                    (body: => T)
                    (implicit  route: Route): T = Delete(endpoint) ~> Route.seal(route) ~> check(body)
}

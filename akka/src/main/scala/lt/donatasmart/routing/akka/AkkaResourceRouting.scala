package lt.donatasmart.routing.akka

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import lt.donatasmart.routing.entities.{ResourceItem, RouteConfig}
import lt.donatasmart.routing.service.Service

final class AkkaResourceRouting(val service: Service,
                                val config: RouteConfig = RouteConfig()) extends CustomRouting {

  lazy val route: Route = resourceRouting.foldLeft(ping) { case (acc, item) =>
    acc ~ item.resourcePaths.fold(
      e => complete(StatusCodes.InternalServerError, e.message),
      paths => paths.tail.foldLeft(pathPrefix(paths.head)) { case (folded, pathItem) =>
        args = collection.mutable.Map.empty[String, Any]

        pathItem match {
          case ResourceItem.id => folded & pathPrefix(LongNumber).flatMap(withId)
          case ResourceItem.token => folded & pathPrefix(Segment).flatMap(withToken)
          case pathString => folded & pathPrefix(pathString)
        }
      } {
        pathEnd {
          execMethod(args, item)
        }
      }
    )
  }
}

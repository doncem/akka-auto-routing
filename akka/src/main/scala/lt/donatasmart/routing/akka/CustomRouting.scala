package lt.donatasmart.routing.akka

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive0, Directives, Route}
import io.circe.Encoder
import lt.donatasmart.routing.ResourceRouting
import lt.donatasmart.routing.entities.{Method, ResourceItem}
import lt.donatasmart.routing.http.JsonSupport
import lt.donatasmart.routing.service.Service

import collection.mutable.{Map => mMap}
import util.{Failure, Success, Try}

trait CustomRouting extends ResourceRouting with Directives with JsonSupport {

  def service: Service
  private lazy val methods: Array[java.lang.reflect.Method] = service.getClass.getMethods
  implicit lazy val encoder: Encoder[AnyRef] = service.encoder

  var args: mMap[String, Any] = _

  // extra paths

  protected def withId(id: Long): Directive0 = {
    args.+=(("id", id))
    pass
  }

  protected def withToken(token: String): Directive0 = {
    args.+=(("token", token))
    pass
  }

  // main exec
  protected def execMethod(args: mMap[String, Any], item: ResourceItem): Route = (item.method match {
    case Method.DELETE => delete
    case Method.GET => get
    case Method.POST => post
    case Method.PUT => put
  }) {
    methods.find(_.getName == item.callback) match {
      case None => complete(StatusCodes.InternalServerError, "Callback method not found")
      case Some(method) => Try(method.invoke(service, args.toMap)) match {
        case Failure(e) =>
          logger.error("Invocation Error", e)
          complete(StatusCodes.InternalServerError, Option(e.getMessage).getOrElse(e.getCause.getMessage))
        case Success(result) => complete(result)
      }
    }
  }

  lazy val ping: Route = path("ping") {
    complete("pong")
  }

  def route: Route
}

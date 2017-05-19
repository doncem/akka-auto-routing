package lt.donatasmart.routing.http

import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromResponseUnmarshaller, Unmarshal, Unmarshaller}
import akka.http.scaladsl.util.FastFuture._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import lt.donatasmart.routing.entities.{Error, LibraryError, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

trait JsonSupport extends FailFastCirceSupport {

  import JsonSupport._

  implicit def responseUnmarshaller[A, B](implicit ua: FromResponseUnmarshaller[A],
                                          ub: FromResponseUnmarshaller[B]): FromResponseUnmarshaller[Either[A, B]] =
    Unmarshaller.withMaterializer { implicit ec => implicit mat => response =>
      if (response.status.isSuccess) ub(response).map(Right(_)) else ua(response).map(Left(_))
    }

  implicit def enrichUnmarshal[A](unmarshal: Unmarshal[A]) = new {
    def toResult[B, C](implicit um: Unmarshaller[A, Either[B, C]],
                       ec: ExecutionContext,
                       mat: Materializer): Future[Either[B, C]] = um(unmarshal.value)
  }

  implicit def futureMarshaller[A, B](implicit m: Marshaller[Result[A], B]): Marshaller[Future[Result[A]], B] =
    Marshaller(implicit ec => _.fast.recover(handleFailure).flatMap(m(_)))

  implicit def resultMarshaller[A, B](implicit ma: Marshaller[(StatusCode, A), B],
                                      me: Marshaller[(StatusCode, Error), B]): Marshaller[Result[A], B] =
    Marshaller(implicit ec =>  _.fold(e => me(handleError(e)), r => ma(handleResult(r))))
}

object JsonSupport {

  private[http] def handleResult[A](result: A): (StatusCode, A) = result match {
    case None | Nil => StatusCodes.NoContent -> Option.empty[A].orNull
    case _ => StatusCodes.OK -> result
  }

  private[http] def handleError(error: Error): (StatusCode, Error) = error match {
    case _ => StatusCodes.InternalServerError -> error
  }

  private[http] def handleFailure[A >: Error, B] = PartialFunction[Throwable, Either[A, B]] { t =>
    Left(LibraryError("There was an internal server error"))
  }
}

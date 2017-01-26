package lt.donatasmart.routing.entities

import cats.data.NonEmptyList

object ResourceItem {
  val id: String = "{{id}}"
  val token: String = "{{token}}"
}

case class ResourceItem(resource: String, method: Method.Value, callback: String) {
  val resourcePaths: Result[NonEmptyList[String]] = resource.split("\\/").filterNot(_.isEmpty).toList match {
    case head :: tail => Right(NonEmptyList(head, tail))
    case _ => Left(RequirementError(s"Resource definition is not complete: '$resource'"))
  }
}

package lt.donatasmart.routing.entities

import cats.data.NonEmptyList

import scala.util.matching.Regex

object ResourceItem {
  val resourceTypeLong: String = "long"
  val resourceTypeInt: String = "int"
  val resourceTypeString: String = "string"

  val resourceParamRegex: Regex = raw"""\{\{([a-z][0-9A-Za-z]*):($resourceTypeInt|$resourceTypeLong|$resourceTypeString)\}\}""".r
}

case class ResourceItem(resource: String, method: Method.Value, callback: String) {
  val resourcePaths: Result[NonEmptyList[String]] = resource.split("\\/").filterNot(_.isEmpty).toList match {
    case head :: tail => Right(NonEmptyList(head, tail))
    case _ => Left(RequirementError(s"Resource definition is not complete: '$resource'"))
  }
}

package lt.donatasmart.routing.parser

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import lt.donatasmart.routing.entities.{RequirementError, ResourceItem, Result}

trait Parser extends LazyLogging {
  val extension: String

  def parse(file: File): Parser.Items
}

object Parser extends LazyLogging {
  type Items = Result[Iterable[ResourceItem]]
  private val jsonParser: Parser = new JsonParser

  def parse(file: File): Items = file.getName.split("\\.").lastOption match {
    case Some(jsonParser.extension) => jsonParser.parse(file)
    case Some(extension) => Left(RequirementError(s"File parser for '$extension' does not exist"))
    case None => Left(RequirementError("Badly named file - no extension"))
  }
}

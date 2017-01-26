package lt.donatasmart.routing.parser

import java.io.File

import io.circe.{Decoder, DecodingFailure}
import io.circe.parser.decode
import lt.donatasmart.routing.entities.{ExtractionError, Method, ResourceItem}

import scala.io.Source

class JsonParser extends Parser {
  implicit val methodDecoder: Decoder[Method.Value] = Decoder.instance(cursor =>
    cursor.as[String].flatMap(Method.find).fold(
      e => Left(DecodingFailure(e.toString, cursor.history)),
      Right.apply
    )
  )
  implicit val resourceItemDecoder: Decoder[ResourceItem] = Decoder.instance(cursor =>
    (cursor.downField("resource").as[String], cursor.downField("method").as[Method.Value], cursor.downField("callback").as[String]) match {
      case (Right(resource), Right(method), Right(callback)) => Right(new ResourceItem(resource, method, callback))
      case _ => Left(DecodingFailure("Failed to decode resource", cursor.history))
    }
  )

  val extension: String = "json"

  def parse(file: File): Parser.Items = decode[List[ResourceItem]](Source.fromFile(file).mkString).fold(
    e => Left(ExtractionError(e.getMessage)),
    Right.apply
  )
}

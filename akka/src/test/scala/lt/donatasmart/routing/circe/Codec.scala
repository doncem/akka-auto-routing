package lt.donatasmart.routing.circe

import io.circe.{Encoder, Json}

trait Codec {

  val encoder: Encoder[AnyRef] = Encoder.instance {
    case i: Int with AnyRef => Json.fromInt(i)
    case _ => ???
  }
}

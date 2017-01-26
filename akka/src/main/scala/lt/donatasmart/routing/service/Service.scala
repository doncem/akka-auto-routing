package lt.donatasmart.routing.service

import io.circe.Encoder

abstract class Service {
  def encoder: Encoder[AnyRef]
}

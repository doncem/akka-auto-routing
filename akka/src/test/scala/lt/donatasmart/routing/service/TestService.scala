package lt.donatasmart.routing.service

import lt.donatasmart.routing.circe.Codec

class TestService extends Service with Codec {

  def getTest1Resources(args: Map[String, Any]): Int = 1
  def postTest1Resources(args: Map[String, Any]): Int = 1
  def deleteTest1Resource(args: Map[String, Any]): Int = 1
}

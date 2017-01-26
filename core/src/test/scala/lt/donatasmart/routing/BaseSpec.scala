package lt.donatasmart.routing

import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

trait BaseSpec extends FunSpec with ScalaFutures with Matchers {
  implicit override val patienceConfig = PatienceConfig(Span(10, Seconds), Span(250, Millis))
}

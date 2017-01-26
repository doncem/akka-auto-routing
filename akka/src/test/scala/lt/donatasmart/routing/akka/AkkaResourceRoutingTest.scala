package lt.donatasmart.routing.akka

import akka.http.scaladsl.model.StatusCodes
import lt.donatasmart.routing.BaseSpec
import lt.donatasmart.routing.service.TestService

class AkkaResourceRoutingTest extends BaseSpec with RoutingSpec {

  val routing = new AkkaResourceRouting(new TestService)

  describe("Automatic routing test") {
    it("should ping back") {
      checkGet("/ping") {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe """"pong""""
      }
    }

    it("should not find specified route from the available resource list") {
      checkGet("/test/test/test") {
        status shouldBe StatusCodes.NotFound
      }
    }

    it("should get the simple route and execute the callback") {
      checkGet("/test1/resources") {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "1"
      }
    }

    it("should post route and execute the callback") {
      checkPost("/test1/resources") {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "1"
      }
    }

    it("should delete route and execute the callback") {
      checkDelete("/test1/resources/1") {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "1"
      }
    }
  }
}

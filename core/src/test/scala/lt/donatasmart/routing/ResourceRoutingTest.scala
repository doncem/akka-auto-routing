package lt.donatasmart.routing

import lt.donatasmart.routing.entities.RouteConfig

class ResourceRoutingTest extends BaseSpec {

  describe("Resource") {
    it("should load config by default") {
      val routing = new ResourceRouting {
        lazy val config: RouteConfig = RouteConfig()
      }

      routing.resourceRouting should be('nonEmpty)
    }

    it("should load config with correctly defined resource") {
      val routing = new ResourceRouting {
        lazy val config: RouteConfig = RouteConfig("routes/test-route-1.json")
      }

      routing.resourceRouting should be('nonEmpty)
    }

    it("fail to load resource with incorrect resource path") {
      val routing = new ResourceRouting {
        lazy val config: RouteConfig = RouteConfig("routess")
      }

      routing.resourceRouting should be('empty)
    }
  }
}

package lt.donatasmart.routing.entities

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

case class RouteConfig(routePath: String)

object RouteConfig {

  private implicit class ExtendedConfig(config: Config) {
    def getString(path: String, default: String): String = Try(config.getString(path)).getOrElse(default)
  }

  def apply(config: Config): RouteConfig = RouteConfig(config.getString("dm.routes", "routes"))
  def apply(): RouteConfig = RouteConfig(ConfigFactory.load())
}

package lt.donatasmart.routing

import java.io.File
import java.nio.file.Paths

import com.typesafe.scalalogging.LazyLogging
import lt.donatasmart.routing.entities.{ResourceItem, RouteConfig}
import lt.donatasmart.routing.parser.Parser

import scala.util.Try

trait ResourceRouting extends LazyLogging {
  def config: RouteConfig

  val resourceRouting: Iterable[ResourceItem] = {
    def getRouteItems(dir: File): Iterable[ResourceItem] = (dir.isDirectory, dir.isFile) match {
      case (true, false) => dir.listFiles().flatMap(getRouteItems)
      case (false, true) => Parser.parse(dir).fold(
        e => {
          logger.error(s"Failed to parse '$dir': {}", e.message)
          Nil
        },
        l => l
      )
      case _ => List.empty
    }

    Try(Paths.get(getClass.getClassLoader.getResource(config.routePath).getPath).toFile).fold(
      e => {
        logger.error(s"Could not find the routing resource directory '${config.routePath}'", e)
        Nil
      },
      getRouteItems
    )
  }
}

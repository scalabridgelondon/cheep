package cheep

import scala.util.Properties

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.staticcontent._

/** This service is responsible for serving static files. We serve anything
  * found in the directory specified by the "cheep.assets" systems property,
  * falling back to "./assets/" if that property is not set.
  */
object AssetService {
  val assetDirectory = Properties.propOrElse("cheep.assets", "./assets/")

  def service(): HttpRoutes[IO] =
    fileService(FileService.Config(assetDirectory))
}

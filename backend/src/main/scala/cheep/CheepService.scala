package cheep

import cheep.data._

import cats.effect.IO
import org.http4s.HttpRoutes

/** This service provides the API for the user interface. Data is sent over HTTP
  * as JSON.
  */
final case class CheepService(model: Model) {
  import org.http4s.dsl.io._
  import org.http4s.circe.CirceEntityCodec._
  import org.http4s.implicits._

  val service: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "api" / "posts" => Ok(model.posts)
      case req @ POST -> Root / "api" / "post" =>
        Ok(req.as[Post].map(post => model.create(post)))
    }
}

package cheep

import cheep.data._

import cats.effect.IO
import org.http4s.HttpRoutes

/** This service provides the API for the user interface. Data is sent over HTTP
  * as JSON.
  */
class CheepService(model: Model) {
  import org.http4s.dsl.io._
  import org.http4s.implicits._

  val service: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "api" / "posts" => Ok(model.posts)
      case req @ POST -> Root / "api" / "post" =>
        for {
          post <- req.as[Post]
          id = Model.create(post)
        } yield Ok(id)
    }
}

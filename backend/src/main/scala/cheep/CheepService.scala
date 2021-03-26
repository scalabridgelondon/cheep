package cheep

import cheep.data._

import cats.effect.IO
import org.http4s.HttpRoutes

/** This service provides the API for the user interface. Data is sent over HTTP
  * as JSON.
  */
object CheepService {
  import org.http4s.dsl.io._
  import org.http4s.circe.CirceEntityEncoder._
  object Description extends QueryParamDecoderMatcher[String]("description")

  val service: HttpRoutes[IO] =
    HttpRoutes.of[IO] { case GET -> Root / "api" / "posts" =>
      Ok(Id(1))
    }
}

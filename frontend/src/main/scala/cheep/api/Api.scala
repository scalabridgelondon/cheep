package cheep.api

import cheep.data._

import cats.effect.IO
import io.circe.{Decoder, Json}
import sttp.client3._
import sttp.client3.circe._

object Api {
  val host = "http://localhost:3000"

  val backend = sttp.client3.impl.cats.FetchCatsBackend[IO]()

  def posts: IO[Posts] = {
    val request =
      basicRequest.get(uri"${host}/api/posts").response(asJson[Posts].getRight)

    request.send(backend).map(response => response.body)
  }

  def create(post: Post): IO[Id] = {
    val request =
      basicRequest
        .post(uri"${host}/api/post")
        .body(post)
        .response(asJson[Id].getRight)

    request.send(backend).map(response => response.body)
  }
}

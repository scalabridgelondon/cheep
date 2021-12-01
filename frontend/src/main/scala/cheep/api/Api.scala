package cheep.api

import cheep.data._

import cats.effect.IO
import io.circe.{Decoder, Json}
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.dsl.io._
import org.http4s.dom._
import org.http4s.Method._
import org.http4s.syntax.all._

object Api {
  val host = uri"http://localhost:3000"

  val client = FetchClientBuilder[IO].create

  def posts: IO[Posts] = client.expect(host / "api" / "posts")

  def create(post: Post): IO[Id] =
    client.expect(POST(post, host / "api" / "posts"))

}

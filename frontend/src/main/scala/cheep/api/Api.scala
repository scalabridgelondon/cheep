package cheep.api

import scala.concurrent.Future

import cheep.data._

import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Decoder, Json}
import org.scalajs.dom._

object Api {
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  val host = "http://localhost:3000"

  def applicationJson = {
    val h = new Headers()
    h.append("Content-Type", "application/json")
    h
  }

  def parseJson(string: String): Future[Json] =
    Future {
      parse(string) match {
        case Left(error)  => throw error
        case Right(value) => value
      }
    }

  def posts: Future[Posts] = {
    val ri = defaultRequest
    ri.method = HttpMethod.GET
    ri.mode = RequestMode.cors

    Fetch
      .fetch(s"${host}/api/posts", ri)
      .toFuture
      .flatMap(r => r.text().toFuture)
      .flatMap(s => parseJson(s))
      .map { j =>
        Decoder[Posts].decodeJson(j) match {
          case Left(err)    => throw err
          case Right(tasks) => tasks
        }
      }
  }

  def create(post: Post): Future[Id] = {
    val ri = defaultRequest
    ri.method = HttpMethod.POST
    ri.mode = RequestMode.cors
    ri.headers = applicationJson
    ri.body = post.asJson.spaces2

    val r = new Request(s"${host}/api/post", ri)

    Fetch
      .fetch(r)
      .toFuture
      .flatMap(r => r.text().toFuture)
      .flatMap(s => parseJson(s))
      .map(j =>
        Decoder[Id].decodeJson(j) match {
          case Left(err) => throw err
          case Right(id) => id
        }
      )
  }

  def delete(id: Id): Future[Unit] = {
    val ri = defaultRequest
    ri.method = HttpMethod.DELETE
    ri.mode = RequestMode.cors

    val r = new Request(s"${host}/api/task/${id.id}", ri)

    Fetch
      .fetch(r)
      .toFuture
      .map(r => if (r.ok) () else throw new Exception("Delete failed"))
  }

  def defaultRequest: RequestInit =
    new RequestInit {}
}

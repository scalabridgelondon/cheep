package cheep

import cheep.data.*

import cats.data.OptionT
import cats.effect.IO
import munit.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.circe.*
import org.http4s.implicits.*

class CheepServiceSuite extends CatsEffectSuite {

  test("posts returns expected posts") {
    val request =
      Request[IO](
        method = Method.GET,
        uri = uri"http://localhost:3000/api/posts"
      )

    val optResponse: OptionT[cats.effect.IO, Response[cats.effect.IO]] =
      CheepService(InMemoryModel).service(request)

    optResponse.value
      .flatMap { optResponse =>
        optResponse match {
          case None =>
            IO.raiseError(new Exception("Did not receive a response"))
          case Some(response) => response.as[Posts]
        }
      }
      .assertEquals(InMemoryModel.posts)
  }

  test("post returns an Id") {
    val request =
      Request[IO](
        method = Method.POST,
        body =
          jsonEncoderOf[IO, Post].toEntity(Post("Example", "Text here")).body,
        uri = uri"http://localhost:3000/api/post"
      )

    val optResponse: OptionT[cats.effect.IO, Response[cats.effect.IO]] =
      CheepService(InMemoryModel).service(request)

    optResponse.value.flatMap { optResponse =>
      optResponse match {
        case None =>
          IO.raiseError(new Exception("Did not receive a response"))
        case Some(response) => response.as[Id].as(true)
      }
    }.assert
  }
}

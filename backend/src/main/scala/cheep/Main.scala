package cheep

import cats.effect._
import cats.implicits._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.CORS
import org.http4s.server.{Router, Server}

/** This object setups and runs the webserver.
  */
object Main extends IOApp {
  private def app(): HttpApp[IO] = {
    val services =
      (CORS(CheepService.service) <+> AssetService.service())
    Router("/" -> services).orNotFound
  }

  private def server(): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"3000")
      .withHttpApp(app())
      .build

  private val program: Stream[IO, Unit] =
    for {
      // blocker <- Stream.resource(Blocker[IO])
      server <- Stream.resource(server())
      _ <- Stream.eval(IO(println(s"Started server on: ${server.address}")))
      _ <- Stream.never[IO].covaryOutput[Unit]
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.compile.drain.as(ExitCode.Success)
}

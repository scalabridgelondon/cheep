package cheep.data

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class Post(title: String, body: String)
object Post {
  implicit val postDecoder: Decoder[Post] = deriveDecoder[Post]
  implicit val postEncoder: Encoder[Post] = deriveEncoder[Post]
}

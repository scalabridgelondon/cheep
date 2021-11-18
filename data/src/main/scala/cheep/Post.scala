package cheep.data

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class Post(author: String, text: String)
object Post {
  implicit val postDecoder: Decoder[Post] = deriveDecoder[Post]
  implicit val postEncoder: Encoder[Post] = deriveEncoder[Post]
}

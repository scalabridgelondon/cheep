package cheep.data

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class Posts(posts: Seq[(Id, Post)])
object Posts {
  implicit val postDecoder: Decoder[Posts] = deriveDecoder[Posts]
  implicit val postEncoder: Encoder[Posts] = deriveEncoder[Posts]

  val empty: Posts = Posts(List.empty)
}

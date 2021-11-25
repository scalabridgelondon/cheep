package cheep.data

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import monocle.Lens

final case class Post(author: String, text: String)
object Post {
  implicit val postDecoder: Decoder[Post] = deriveDecoder[Post]
  implicit val postEncoder: Encoder[Post] = deriveEncoder[Post]

  val author = Lens[Post, String](_.author)(a => _.copy(author = a))
  val text = Lens[Post, String](_.text)(t => _.copy(text = t))

  def empty: Post = Post("", "")
}

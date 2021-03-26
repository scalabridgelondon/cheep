package cheep.data

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class Id(id: Int)
object Id {
  implicit val idDecoder: Decoder[Id] = deriveDecoder[Id]
  implicit val idEncoder: Encoder[Id] = deriveEncoder[Id]
  implicit val idOrdering: Ordering[Id] = Ordering.by(id => id.id)
}

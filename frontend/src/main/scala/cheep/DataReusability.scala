package cheep

import cheep.data._

/** Reusability instances for cheep.data */
object DataReusability {
  import japgolly.scalajs.react.Reusability

  implicit val idReusability: Reusability[Id] = Reusability.derive[Id]
  implicit val postReusability: Reusability[Post] = Reusability.derive[Post]
  implicit val postsReusability: Reusability[Posts] = Reusability.derive[Posts]
}

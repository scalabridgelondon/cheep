package cheep

import cheep.data._

/** Reusability instances for cheep.data */
object DataReusability {
  import japgolly.scalajs.react.Reusability

  implicit val idReusability = Reusability.derive[Id]
  implicit val postReusability = Reusability.derive[Post]
  implicit val postsReusability = Reusability.derive[Posts]
}

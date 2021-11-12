package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PostList {
  type Props = Unit
  val component = ScalaComponent
    .static(<.p("Post list goes here"))
}

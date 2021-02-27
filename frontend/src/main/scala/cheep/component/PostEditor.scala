package cheep.component

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

@react object PostEditor {
  type Props = Unit
  val component = FunctionalComponent[Props]{ _ =>
    p("post editor goes here")
  }
}

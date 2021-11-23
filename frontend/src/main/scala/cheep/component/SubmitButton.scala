package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object SubmitButton {
  final case class Props(active: Boolean)

  val component = ScalaComponent
    .builder[Props]
    .render_P(props => <.input.submit())
}

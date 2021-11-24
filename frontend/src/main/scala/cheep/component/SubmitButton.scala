package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object SubmitButton {
  final case class Props(label: String, active: Boolean)

  val component = ScalaComponent
    .builder[Props]
    .render_P(props =>
      <.input.button(
        ^.className := "rounded p-2",
        ^.`type` := "button",
        ^.value := props.label
      )
    )
    .build
}

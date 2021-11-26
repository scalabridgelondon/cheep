package cheep.component

import cats.effect.SyncIO
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object SubmitButton {
  final case class Props(label: String, active: Boolean, onClick: SyncIO[Unit])

  val component = ScalaComponent
    .builder[Props]
    .render_P { props =>
      val css =
        if (props.active)
          "rounded p-2 bg-green-600 hover:bg-green-400 text-white"
        else "rounded p-2 bg-gray-400 hover:bg-gray-400 text-white"
      <.input.button(
        ^.className := css,
        ^.`type` := "button",
        ^.value := props.label,
        ^.onClick -->? (if (props.active) Some(props.onClick) else None)
      )
    }
    .build
}

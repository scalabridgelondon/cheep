package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._

object StringEditor {
  final case class Props(
      name: String,
      label: String,
      string: StateSnapshot[String]
  )

  val component = ScalaComponent
    .builder[Props]
    .render_P(props =>
      <.div(^.className := "py-2")(
        <.div(<.label(^.`for` := props.name)(props.label)),
        <.input.text(
          ^.className := "rounded border-2 border-gray-300 focus:border-red-500 my-2 p-2",
          ^.name := props.name,
          ^.value := props.string.value,
          ^.onChange ==> ((e: ReactEventFromInput) =>
            props.string.setState(e.target.value)
          )
        )
      )
    )
    .build
}

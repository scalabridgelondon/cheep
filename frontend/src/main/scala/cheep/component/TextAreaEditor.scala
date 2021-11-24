package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.StateSnapshot

object TextAreaEditor {
  final case class Props(
      name: String,
      label: String,
      rows: Int,
      cols: Int,
      text: StateSnapshot[String]
  )

  val component = ScalaComponent
    .builder[Props]
    .render_P(props =>
      <.div(^.className := "py-2")(
        <.div(<.label(^.`for` := props.name)(props.label)),
        <.textarea(
          ^.className := "rounded border-2 border-gray-300 focus:border-red-500 my-2 p-2",
          ^.rows := props.rows,
          ^.cols := props.cols,
          ^.value := props.text.value,
          ^.onChange ==> ((e: ReactEventFromInput) =>
            props.text.setState(e.target.value)
          )
        )
      )
    )
    .build
}

package cheep.component

import japgolly.scalajs.react.extra.StateSnapshot

object TextAreaEditor {
  final case class Props(rows: Int, cols: Int, text: StateSnapshot[String])

  val component = ScalaComponent
    .builder[Props]
    .render_P(props =>
      <.textarea(
        ^.className := "rounded border-2 border-gray-300 focus:border-red-500 m-2 p-2",
        ^.rows := props.rows,
        ^.cols := props.cols,
        ^.value := props.text.value,
        ^.onChange ==> ((e: ReactEventFromInput) =>
          props.text.setState(e.target.value)
        )
      )
    )
    .build
}

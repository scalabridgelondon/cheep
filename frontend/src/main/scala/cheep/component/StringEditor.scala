package cheep.component

object StringEditor {
  val component = ScalaComponent
    .builder[StateSnapshot[String]]
    .render_P(snapshot =>
      <.input.text(
        ^.className := "rounded border-2 border-gray-300 focus:border-red-500 m-2 p-2",
        ^.value := snapshot.value,
        ^.onChange ==> ((e: ReactEventFromInput) =>
          snapshot.setState(e.target.value)
        )
      )
    )
    .build
}

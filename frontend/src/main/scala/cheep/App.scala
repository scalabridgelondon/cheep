package cheep

import cheep.component._
import cheep.data._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object App {
  final class Backend($ : BackendScope[Unit, Posts]) {
    def render(state: Posts): VdomElement = {
      <.div(^.className := "container mx-auto py-4")(
        <.div(^.className := "pb-8")(
          <.h1(^.className := "text-4xl font-extrabold")(
            "Cheep!",
            <.span(^.className := "pl-2 text-2xl text-gray-500")(
              "Microblogging on the cheap"
            )
          ),
          <.small(^.className := "text-gray-700")(
            "Cheep is a ",
            <.a(
              ^.className := "text-red-500",
              ^.href := "https://www.scalabridgelondon.org/"
            )(
              "ScalaBridge London"
            ),
            " project. The code is ",
            <.a(
              ^.className := "text-red-500",
              ^.href := "https://github.com/scalabridgelondon/cheep"
            )(
              "open source."
            )
          )
        ),
        PostList.component(state),
        PostEditor.component()
      )
    }
  }
  val component = ScalaComponent
    .builder[Unit]
    .initialState(
      Posts(
        List(
          Id(2) -> Post("BizzBuzz", "When's lunch? I'm sooooo hungry"),
          Id(1) -> Post(
            "Dreamer",
            "The clouds are so beautiful today. It's a good day to be alive!"
          ),
          Id(0) -> Post("BizzBuzz", "Learning http4s today!")
        )
      )
    )
    .renderBackend[Backend]
    .build
}

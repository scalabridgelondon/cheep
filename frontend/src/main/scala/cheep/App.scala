package cheep

import cheep.component._
import cheep.data._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.ReactMonocle._
import monocle.Lens

object App {
  final case class State(posts: Posts, newPost: Editable[Post])
  object State {
    import cheep.DataReusability._
    implicit val stateReusability = Reusability.derive[State]

    val newPost =
      Lens[State, Editable[Post]](_.newPost)(np => s => s.copy(newPost = np))
  }

  final class Backend($ : BackendScope[Unit, State]) {
    val stateSnapshot = StateSnapshot.withReuse.prepareVia($)

    def render(state: State): VdomElement = {
      val foo: StateSnapshot[Editable[Post]] =
        stateSnapshot(state).zoomStateL(State.newPost)

      <.div(^.className := "container mx-auto py-4")(
        <.div(^.className := "pb-6")(
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
        PostList.component(state.posts),
        PostEditor.component(foo)
      )
    }
  }
  val component = ScalaComponent
    .builder[Unit]
    .initialState(
      State(
        Posts(
          List(
            Id(5) -> Post(
              "Albert",
              "In the midst of winter, I found there was, within me, an invincible summer."
            ),
            Id(4) -> Post("BizzBuzz", "When's lunch? I'm sooooo hungry"),
            Id(3) -> Post(
              "Archimedes",
              "Give me a lever and a place to stand, and I will move the world!"
            ),
            Id(2) -> Post(
              "Dreamer",
              "The clouds are so beautiful today. It's a good day to be alive!"
            ),
            Id(1) -> Post("BizzBuzz", "Learning http4s today!"),
            Id(0) -> Post(
              "Ada",
              "That brain of mine is something more than merely mortal, as time will show."
            )
          )
        ),
        Editable.draft(Post.empty)
      )
    )
    .renderBackend[Backend]
    .build
}

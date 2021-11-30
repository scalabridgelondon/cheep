package cheep

import cheep.api.Api
import cheep.component._
import cheep.data._

import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.ReactMonocle._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._
import monocle.Lens

object App {
  import japgolly.scalajs.react.util.EffectCatsEffect._

  final case class State(posts: Posts, newPost: Post)
  object State {
    import cheep.DataReusability._
    implicit val stateReusability: Reusability[State] =
      Reusability.derive[State]

    val posts = Lens[State, Posts](_.posts)(ps => s => s.copy(posts = ps))
    val newPost =
      Lens[State, Post](_.newPost)(np => s => s.copy(newPost = np))
  }

  final class Backend($ : BackendScope[Unit, State]) {
    def fetchAndSetPosts(posts: StateSnapshot[Posts]): IO[Unit] =
      Api.posts.flatMap(ps => posts.setState(ps).to[IO])

    def render(state: State): VdomElement = {
      val postsSnapshot: StateSnapshot[Posts] =
        StateSnapshot.zoomL(State.posts)(state).setStateVia($)

      val newPostSnapshot: StateSnapshot[Post] =
        StateSnapshot.zoomL(State.newPost)(state).setStateVia($)

      def onPost(post: Post): IO[Unit] = {
        Api.create(post).flatMap(_ => fetchAndSetPosts(postsSnapshot))
      }

      println(s"State is $state")

      <.div(^.className := "container mx-auto p-4")(
        <.div(^.className := "mb-6 p-4 rounded bg-white")(
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
        <.div(^.className := "mb-4 p-4 rounded bg-white")(
          PostList.component(postsSnapshot)
        ),
        <.div(^.className := "mb-4 p-4 rounded bg-white")(
          PostEditor.component(
            PostEditor.Props(newPostSnapshot, onPost)
          )
        )
      )
    }
  }
  val component = ScalaComponent
    .builder[Unit]("Cheep")
    .initialState(State(Posts(List.empty), Post.empty))
    .renderBackend[Backend]
    .componentDidMount($ =>
      $.backend.fetchAndSetPosts(
        StateSnapshot.zoomL(State.posts)($.state).setStateVia($)
      )
    )
    .build
}

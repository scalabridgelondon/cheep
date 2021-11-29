package cheep.component

import cheep.data.Post

import cats.effect.SyncIO
import japgolly.scalajs.react.ReactMonocle._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._
// import japgolly.scalajs.react.callback.Callback

object PostEditor {
  final case class Props(
      currentPost: StateSnapshot[Post],
      onPost: Post => SyncIO[Unit]
  )

  final class Backend($ : BackendScope[Props, Unit]) {
    val authorSnapshot = StateSnapshot.withReuse
      .zoomL(Post.author)
      .prepareViaProps($)(_.currentPost)

    val textSnapshot = StateSnapshot.withReuse
      .zoomL(Post.text)
      .prepareViaProps($)(_.currentPost)

    def render(props: Props): VdomElement = {
      def postIsValid(post: Post): Boolean =
        post.author.nonEmpty && post.text.nonEmpty

      val onSubmit: SyncIO[Unit] =
        props.onPost(props.currentPost.value) >>
          props.currentPost.setState(Post.empty)

      <.div(^.className := "py-4")(
        <.h3(^.className := "text-2xl font-extrabold pb-2")(
          "Say something. Let the world know."
        ),
        StringEditor.component(
          StringEditor.Props(
            name = "author",
            label = "Author",
            string = authorSnapshot(props.currentPost.value)
          )
        ),
        TextAreaEditor.component(
          TextAreaEditor.Props(
            name = "content",
            label = "Post",
            rows = None,
            cols = None,
            text = textSnapshot(props.currentPost.value)
          )
        ),
        SubmitButton.component(
          SubmitButton.Props(
            label = "Post it!",
            active = postIsValid(props.currentPost.value),
            onClick = onSubmit
          )
        )
      )
    }
  }

  val component =
    ScalaComponent
      .builder[Props]
      .renderBackend[Backend]
      .build

}

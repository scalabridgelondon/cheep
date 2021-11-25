package cheep.component

import cheep.Editable
import cheep.data.Post
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.ReactMonocle._
import monocle._
import japgolly.scalajs.react.callback.Callback

object PostEditor {
  import Editable._

  type Props = StateSnapshot[Editable[Post]]

  val postLens: Lens[Editable[Post], Post] = Editable.lens(Post("", ""))

  final class Backend($ : BackendScope[Props, Unit]) {
    val author: Lens[Editable[Post], String] = postLens.andThen(Post.author)
    val text: Lens[Editable[Post], String] = postLens.andThen(Post.text)

    val authorSnapshot = StateSnapshot.withReuse
      .zoomL(author)
      .prepareViaProps($)((props: Props) => props)

    val textSnapshot = StateSnapshot.withReuse
      .zoomL(text)
      .prepareViaProps($)((props: Props) => props)

    def render(props: Props): VdomElement = {
      def postIsValid(post: Post): Boolean =
        post.author.nonEmpty && post.text.nonEmpty

      def onSubmit: Callback =
        Callback {
          props.value match {
            case Finished(post) => ()
            case d @ Draft(post) =>
              if (postIsValid(post)) props.setState(d.finish) else ()
          }
        }

      <.div(^.className := "py-4")(
        <.h3(^.className := "text-2xl font-extrabold pb-2")(
          "Say something. Let the world know."
        ),
        StringEditor.component(
          StringEditor.Props(
            name = "author",
            label = "Author",
            string = authorSnapshot(props.value)
          )
        ),
        TextAreaEditor.component(
          TextAreaEditor.Props(
            name = "content",
            label = "Post",
            rows = 5,
            cols = 80,
            text = textSnapshot(props.value)
          )
        ),
        SubmitButton.component(
          SubmitButton.Props(label = "Post it!", active = false)
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

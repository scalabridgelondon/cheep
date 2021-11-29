package cheep.component

import scala.scalajs.js

import cheep.data.Posts

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.StateSnapshot
import japgolly.scalajs.react.vdom.html_<^._

object PostList {
  type Props = StateSnapshot[Posts]

  // Convert a String to CSS HSL string, where the color is chosen based on the
  // content of the input String.
  def stringToHsl(string: String): String = {
    val hue = string.hashCode() % 360
    s"hsl($hue, 70%, 70%)"
  }

  // Convert a string to CSS style
  //
  // This gives us a consistent color for each author without having to store
  // the color for the author
  def stringToStyle(string: String): js.Dictionary[String] = {
    val color = stringToHsl(string)

    js.Dictionary("color" -> color)
  }

  val component = ScalaComponent
    .builder[Props]
    .render_P(posts =>
      posts.value.posts.toVdomArray { case (id, post) =>
        val hsl = stringToHsl(post.author)
        <.div(
          ^.className := "my-4 pl-2 rounded border-l-4",
          ^.style := js.Dictionary("border-color" -> hsl)
        )(
          <.span(^.className := "hidden")(id.id.toString),
          <.small(
            ^.className := "font-bold",
            ^.style := js.Dictionary("color" -> hsl)
          )(post.author),
          <.p(post.text)
        )
      }
    )
    .build
}

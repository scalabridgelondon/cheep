package cheep.component

import scala.scalajs.js

import cheep.data.Posts

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PostList {
  type Props = Posts

  // Convert a string to CSS style
  //
  // This gives us a consistent color for each author without having to store
  // the color for the author
  def stringToStyle(string: String): js.Dictionary[String] = {
    val hue = string.hashCode() % 360

    js.Dictionary(
      "color" -> (s"rgb($hue, 0.7, 0.7)")
    )
  }

  val component = ScalaComponent
    .builder[Props]
    .render_P(posts =>
      posts.posts.toVdomArray { case (id, post) =>
        <.div(^.className := "py-4 border-gray-300 border-t-2")(
          <.span(^.className := "hidden")(id.id.toString),
          <.small(
            ^.className := "font-bold",
            ^.style := stringToStyle(post.author)
          )(post.author),
          <.p(post.text)
        )
      }
    )
    .build
}

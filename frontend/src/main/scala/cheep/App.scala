package cheep

import cheep.component._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import cheep.data.Posts

object App {
  type Props = Unit
  val component = ScalaComponent.static {
    <.div(^.className := "container mx-auto py-4")(
      <.h1(^.className := "text-4xl font-extrabold")("Cheep!"),
      <.h2(^.className := "text-2xl")("Microblogging on the cheap"),
      PostEditor.component(),
      PostList.component(Posts.empty)
    )
  }
}

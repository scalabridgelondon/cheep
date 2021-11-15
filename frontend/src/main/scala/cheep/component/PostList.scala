package cheep.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import cheep.data.Posts

object PostList {
  type Props = Posts
  val component = ScalaComponent
    .builder[Props]
    .render_P(posts =>
      <.div(
        posts.posts.toVdomArray { case (id, post) => <.p(post.body) }
      )
    )
    .build
}

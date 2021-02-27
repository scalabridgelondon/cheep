package cheep

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._

import cheep.component._

@react object App {
  type Props = Unit
  val component = FunctionalComponent[Props]{ _ =>
    div(className := "container mx-auto py-4")(
      h1(className := "text-4xl font-extrabold")("Cheep!"),
      h2(className := "text-2xl")("Microblogging on the cheap"),
      PostEditor(),
      PostList()
    )
  }
}

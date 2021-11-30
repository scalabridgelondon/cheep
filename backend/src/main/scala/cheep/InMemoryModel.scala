package cheep

import scala.collection.concurrent

import cheep.data._

object InMemoryModel extends Model {
  val currentId = new java.util.concurrent.atomic.AtomicInteger(6)
  val postStore = concurrent.TrieMap[Id, Post](
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

  def nextId(): Id = {
    val id = currentId.getAndIncrement()
    Id(id)
  }

  /** Get all the posts */
  def posts: Posts = {
    val sortedPosts: List[(Id, Post)] =
      postStore.toArray.sortInPlaceBy { case (id, _) => id }.toList
    Posts(sortedPosts)
  }

  /** Create a new post */
  def create(post: Post): Id = {
    val id = nextId()
    postStore.addOne(id -> post)
    id
  }
}

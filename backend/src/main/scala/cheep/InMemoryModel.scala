package cheep

import scala.collection.concurrent

import cheep.data._

object InMemoryModel extends Model {
  val currentId = new java.util.concurrent.atomic.AtomicInteger()
  val postStore = concurrent.TrieMap.empty[Id, Post]

  def nextId(): Id = {
    val id = currentId.getAndIncrement()
    Id(id)
  }

  /** Get all the posts */
  def posts: Posts = {
    val sortedPosts: Seq[(Id, Post)] =
      postStore.toArray.sortInPlaceBy { case (id, _) => id }.toSeq
    Posts(sortedPosts)
  }

  /** Create a new post */
  def create(post: Post): Id = {
    val id = nextId()
    postStore.addOne(id -> post)
    id
  }
}

package cheep

import cheep.data.*
import munit.*

class InMemoryModelSuite extends FunSuite {
  test("Same posts are returned if there is no modification between calls") {
    assertEquals(InMemoryModel.posts, InMemoryModel.posts)
  }

  test("Created post is assigned an id that is not already in use") {
    val ids = InMemoryModel.posts.posts.map((id, _) => id)

    val newIds =
      (0.to(1000))
        .map(i => InMemoryModel.create(Post(i.toString, i.toString)))
        .toList

    assertEquals(newIds.intersect(ids), List.empty[Id])
    assertEquals(newIds.distinct.size, newIds.size)
  }

  test("Created post is returned and original posts are unchanged") {
    val originalPosts = InMemoryModel.posts
    val newPost = Post("Example", "Example")
    val newId = InMemoryModel.create(newPost)
    val updatedPosts = InMemoryModel.posts

    assertEquals(updatedPosts.posts.size, originalPosts.posts.size + 1)
    assertEquals(
      updatedPosts.posts.find((id, _) => id == newId),
      Some(newId -> newPost)
    )
    assertEquals(
      updatedPosts.posts.filterNot((id, _) => id == newId),
      originalPosts.posts
    )
  }
}

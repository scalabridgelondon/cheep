package cheep

import cheep.data._

trait Model {
  /** Get all the posts */
  def posts: Posts

  /** Create a new post */
  def create(post: Post): Id
}

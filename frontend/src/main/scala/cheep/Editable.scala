package cheep

import japgolly.scalajs.react.Reusability
import monocle.Lens

/** Represents a value that may be in the process of being edit. Can be in three states:
  *
  * - Empty: there is no value. Editing has not begun
  * - Draft: there is a value that the user is in the process of editing
  * - Final: the user has finalized the value
  */
sealed trait Editable[+A]
object Editable {
  implicit def editableReusability[A](implicit
      ar: Reusability[A]
  ): Reusability[Editable[A]] =
    Reusability[Editable[A]] { (e1, e2) =>
      e1 match {
        case Empty =>
          e2 match {
            case Empty     => true
            case Draft(v2) => false
            case Final(v2) => false
          }
        case Draft(v1) =>
          e2 match {
            case Empty     => false
            case Draft(v2) => ar.test(v1, v2)
            case Final(v2) => false
          }
        case Final(v1) =>
          e2 match {
            case Empty     => false
            case Draft(v2) => false
            case Final(v2) => ar.test(v1, v2)
          }
      }
    }

  val empty: Empty.type = Empty

  def lens[A](empty: => A): Lens[Editable[A], A] =
    Lens[Editable[A], A](_ match {
      case Empty    => empty
      case Draft(a) => a
      case Final(a) => a
    })(a =>
      editable =>
        editable match {
          case Empty    => Draft(a)
          case Draft(_) => Draft(a)
          case Final(_) => Final(a)
        }
    )

  final case class Final[A](value: A) extends Editable[A]
  final case class Draft[A](value: A) extends Editable[A] {
    // Can't be called finalize because that collides with JVM built-in
    def finish: Final[A] = Final(value)
  }
  case object Empty extends Editable[Nothing] {
    def draft[A](value: A): Draft[A] = Draft(value)
  }
}

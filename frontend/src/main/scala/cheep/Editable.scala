package cheep

import japgolly.scalajs.react.Reusability
import monocle.Lens

/** Represents a value that may be in the process of being edited. Can be in
  * two states:
  *
  * - Draft: there is a value that the user is in the process of editing
  * - Finished: the user has finishedized the value
  */
sealed trait Editable[+A] {
  import Editable._

  def isDraft: Boolean =
    this match {
      case Draft(_)    => true
      case Finished(_) => false
    }

  def isFinished: Boolean =
    this match {
      case Draft(_)    => false
      case Finished(_) => true
    }
}
object Editable {
  implicit def editableReusability[A](implicit
      ar: Reusability[A]
  ): Reusability[Editable[A]] =
    Reusability[Editable[A]] { (e1, e2) =>
      e1 match {
        case Draft(v1) =>
          e2 match {
            case Draft(v2)    => ar.test(v1, v2)
            case Finished(v2) => false
          }
        case Finished(v1) =>
          e2 match {
            case Draft(v2)    => false
            case Finished(v2) => ar.test(v1, v2)
          }
      }
    }

  def lens[A](empty: => A): Lens[Editable[A], A] =
    Lens[Editable[A], A](_ match {
      case Draft(a)    => a
      case Finished(a) => a
    })(a =>
      editable =>
        editable match {
          case Draft(_)    => Draft(a)
          case Finished(_) => Finished(a)
        }
    )

  def draft[A](value: A): Editable[A] = Draft(value)
  def finished[A](value: A): Editable[A] = Finished(value)

  final case class Finished[A](value: A) extends Editable[A]
  final case class Draft[A](value: A) extends Editable[A] {
    def finish: Finished[A] = Finished(value)
  }

}

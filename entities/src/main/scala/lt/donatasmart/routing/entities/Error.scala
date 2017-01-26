package lt.donatasmart.routing.entities

sealed trait Error {
  def message: String
}

case class ExtractionError(message: String) extends Error
case class EnumerationError(message: String) extends Error
case class LibraryError(message: String) extends Error
case class RequirementError(message: String) extends Error

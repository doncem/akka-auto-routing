package lt.donatasmart.routing.entities

/*
Missing functionality from scala's Enumeration
 */
trait SafeEnumeration extends Enumeration {

  def find(s: String): Result[Value] = values
    .find(_.toString == s)
    .fold[Result[Value]](Left(EnumerationError(s"Value '$s' not found")))(Right.apply)
}

package lt.donatasmart.routing

package object entities {

  type Result[+T] = Either[Error, T]
}

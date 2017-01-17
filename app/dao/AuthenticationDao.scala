package dao

import models.{AdminUser, RegularUser, User}

import scala.concurrent.Future

/**
  * Created on 1/13/2017.
  */
trait AuthenticationDao {
  def authenticate(username: String, password: String): Future[AuthResult]
}

abstract class AuthResult
case object AuthFailure extends AuthResult
case class AuthSuccess(user: User) extends AuthResult

object AuthenticationDao {
  implicit object HardcodedAuthenticationDao extends AuthenticationDao {

    private val users = Map(
      "vlad" -> User(RegularUser, "vlad"),
      "admin" -> User(AdminUser, "admin"))

    import scala.concurrent.ExecutionContext.Implicits.global

    override def authenticate(username: String, password: String): Future[AuthResult] = Future {
      if(username == password && users.contains(username))
        AuthSuccess(users(username))
      else
        AuthFailure
    }
  }
}
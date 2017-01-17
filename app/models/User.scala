package models

/**
  * Created on 1/13/2017.
  */
case class User(userType: UserType, username: String) {

}

abstract sealed class UserType
case object AdminUser extends UserType
case object RegularUser extends UserType
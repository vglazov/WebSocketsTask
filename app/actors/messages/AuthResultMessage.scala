package actors.messages

import dao.{AuthFailure, AuthResult, AuthSuccess}
import play.api.libs.json._

/**
  * Created on 1/13/2017.
  */
case class AuthResultMessage(authResult: AuthResult) extends ResponseMessage {
  override def toJson: JsObject = authResult match {
    case AuthFailure => Json.obj("$type" -> "login_failed")
    case AuthSuccess(user) => Json.obj("$type" -> "login_success", "user" -> user.username)
  }
}
package actors.messages

import play.api.libs.json.{Json, Reads}

/**
  * Created on 1/13/2017.
  */
case class AuthenticateMessage(username: String, password: String) extends RequestMessage

object AuthenticateMessage {
  implicit val authenticateMessageReads: Reads[AuthenticateMessage] = Json.reads[AuthenticateMessage]
}
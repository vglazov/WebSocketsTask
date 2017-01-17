package actors.messages

import play.api.libs.json.{JsObject, Json, Writes}

/**
  * Created on 1/16/2017.
  */
object NotAuthorizedMessage extends ResponseMessage {
  override def toJson: JsObject = Json.obj("$type" -> "not_authorized")
}
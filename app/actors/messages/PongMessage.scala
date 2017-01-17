package actors.messages

import play.api.libs.json.{JsObject, JsValue, Json, Writes}

/**
  * Created on 1/14/2017.
  */
case class PongMessage(seq: Option[Int]) extends ResponseMessage {
  override def toJson: JsObject = Json.obj(
    "$type" -> "pong",
    "seq" -> seq
  )
}
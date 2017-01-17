package actors.messages
import play.api.libs.json.{JsObject, Json}

/**
  * Created on 1/17/2017.
  */
case class RemovalFailedMessage(message: RemoveTableMessage, cause: Throwable) extends ResponseMessage{
  override def toJson: JsObject = Json.obj(
    "$type" -> "removal_failed",
    "id" -> message.id
  )
}

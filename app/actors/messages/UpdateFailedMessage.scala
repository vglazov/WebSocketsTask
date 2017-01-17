package actors.messages
import play.api.libs.json.{JsObject, Json}

/**
  * Created on 1/17/2017.
  */
case class UpdateFailedMessage(updateTableMessage: UpdateTableMessage, cause: Throwable) extends ResponseMessage {
  override def toJson: JsObject = Json.obj(
    "$type" -> "update_failed",
    "id" -> updateTableMessage.table.id
  )
}

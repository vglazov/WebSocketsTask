package actors.messages

import models.Table
import play.api.libs.json.{JsObject, Json}

/**
  * Created on 1/17/2017.
  */
case class TableRemovedMessage(id: Int) extends ResponseMessage {
  override def toJson: JsObject = Json.obj(
    "$type" -> "table_removed",
    "id" -> id
  )
}

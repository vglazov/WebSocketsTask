package actors.messages

import models.Table
import play.api.libs.json.{JsObject, JsValue, Json, Writes}

/**
  * Created on 1/16/2017.
  */
case class TableAddedMessage(table: Table, afterId: Option[Int]) extends ResponseMessage {
  override def toJson: JsObject = Json.obj(
    "$type" -> "table_added",
    "table" -> table,
    "after_id" -> afterId
  )
}
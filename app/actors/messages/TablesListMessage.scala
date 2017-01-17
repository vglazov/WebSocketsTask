package actors.messages

import models.Table
import play.api.libs.json.{JsArray, JsObject, Json}

/**
  * Created on 1/17/2017.
  */
case class TablesListMessage(tables: Seq[Table]) extends ResponseMessage {
  override def toJson: JsObject = Json.obj(
    "$type" -> "table_list",
    "tables" -> tables
  )
}

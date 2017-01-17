package actors.messages

import models.Table
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  * Created on 1/16/2017.
  */
case class AddTableMessage(table: Table, afterId: Option[Int]) extends RequestMessage

object AddTableMessage {
  implicit val addTableMessageReads: Reads[AddTableMessage] = (
    (JsPath \ "table").read[Table] and
    (JsPath \ "after_id").readNullable[Int]
  )(AddTableMessage.apply _)
}

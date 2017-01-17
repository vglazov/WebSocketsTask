package actors.messages

import models.Table
import play.api.libs.json.{Json, Reads}

/**
  * Created on 1/17/2017.
  */
case class UpdateTableMessage(table: Table) extends RequestMessage

object UpdateTableMessage {
  implicit val updateTableMessageReads: Reads[UpdateTableMessage] = Json.reads[UpdateTableMessage]
}

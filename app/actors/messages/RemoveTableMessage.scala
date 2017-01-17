package actors.messages

import play.api.libs.json.{Json, Reads}

/**
  * Created on 1/17/2017.
  */
case class RemoveTableMessage(id: Int) extends RequestMessage

object RemoveTableMessage {
  implicit val removeTableMessageReads: Reads[RemoveTableMessage] = Json.reads[RemoveTableMessage]
}

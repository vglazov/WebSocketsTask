package actors.messages

import play.api.libs.json._

/**
  * Created on 1/14/2017.
  */
case class PingMessage(seq: Option[Int]) extends RequestMessage

object PingMessage {
  implicit val pingMessageReads: Reads[PingMessage] = Json.reads[PingMessage]
}
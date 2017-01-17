package actors.messages

import play.api.libs.json._

/**
  * Created on 1/13/2017.
  */
trait RequestMessage

object RequestMessage {
  implicit val messageReads: Reads[RequestMessage] = new Reads[RequestMessage] {
    override def reads(msgJson: JsValue): JsResult[RequestMessage] = {
      (msgJson \ "$type").asOpt[String] match {
        case Some("login") => msgJson.validate[AuthenticateMessage]
        case Some("ping") => msgJson.validate[PingMessage]
        case Some("add_table") => msgJson.validate[AddTableMessage]
        case Some("update_table") => msgJson.validate[UpdateTableMessage]
        case Some("remove_table") => msgJson.validate[RemoveTableMessage]
        case Some("subscribe_tables") => JsSuccess(SubscribeTablesMessage)
        case Some("unsubscribe_tables") => JsSuccess(UnsubscribeTablesMessage)
        case Some("test_exception") => JsSuccess(TestExceptionMessage)
        case Some(msgType) => JsError(s"Unexpected message type $msgType")
        case None => JsError("Missing message type")
      }
    }
  }
}
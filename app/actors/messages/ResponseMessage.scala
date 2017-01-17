package actors.messages

import play.api.libs.json.JsObject

/**
  * Created on 1/16/2017.
  */
trait ResponseMessage {
  def toJson: JsObject
}



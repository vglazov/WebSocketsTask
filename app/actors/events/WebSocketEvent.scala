package actors.events

import actors.messages.ResponseMessage
import akka.actor.ActorRef

/**
  * Created on 1/17/2017.
  */
case class WebSocketEvent(actorRef: ActorRef, message: ResponseMessage)

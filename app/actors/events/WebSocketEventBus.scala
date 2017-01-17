package actors.events

import actors.messages.ResponseMessage
import akka.actor.{ActorRef, ActorSystem}
import akka.event.{ActorClassifier, ActorEventBus, ManagedActorClassification}

/**
  * Created on 1/17/2017.
  */
class WebSocketEventBus(val system: ActorSystem) extends ActorEventBus with ActorClassifier with ManagedActorClassification {
  override type Event = WebSocketEvent
  override protected def classify(event: Event): ActorRef = event.actorRef
  override protected def mapSize: Int = 1
}

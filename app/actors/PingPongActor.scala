package actors

import actors.messages.{PingMessage, PongMessage}
import akka.actor.{Actor, Props}
import akka.actor.Actor.Receive

/**
  * Created on 1/14/2017.
  */
class PingPongActor extends Actor{
  override def receive: Receive = {
    case msg: PingMessage => sender ! PongMessage(msg.seq)
  }
}

object PingPongActor {
  val props = Props(new PingPongActor())
}
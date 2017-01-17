package actors

import actors.events.{WebSocketEvent, WebSocketEventBus}
import actors.messages._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.Actor.Receive
import akka.event.{ActorClassifier, ActorEventBus, EventBus, ManagedActorClassification}
import dao.TablesDao

import scala.util.{Failure, Success}

/**
  * Created on 1/16/2017.
  */
class TablesActor(val tablesDao: TablesDao, val eventBus: WebSocketEventBus) extends Actor {

  import context.dispatcher

  override def receive: Receive = {
    case msg: AddTableMessage =>
      val returnTo = sender
      tablesDao.newTable(msg.table, msg.afterId) onComplete {
        case Success(table) =>
          val response = TableAddedMessage(table, msg.afterId)
          returnTo ! response
          eventBus.publish(WebSocketEvent(self, response))
        case Failure(t) =>
          returnTo ! AddFailedMessage(msg, t)
      }
    case msg: UpdateTableMessage =>
      val returnTo = sender
      tablesDao.updateTable(msg.table) onComplete {
        case Success(_) =>
          val response = TableUpdatedMessage(msg.table)
          returnTo ! response
          eventBus.publish(WebSocketEvent(self, response))
        case Failure(t) =>
          returnTo ! UpdateFailedMessage(msg, t)
      }
    case msg: RemoveTableMessage =>
      val returnTo = sender
      tablesDao.removeTable(msg.id) onComplete {
        case Success(_) =>
          val response = TableRemovedMessage(msg.id)
          returnTo ! response
          eventBus.publish(WebSocketEvent(self, response))
        case Failure(t) =>
          returnTo ! RemovalFailedMessage(msg, t)
      }
    case SubscribeTablesMessage =>
      eventBus.subscribe(sender, self)
      val returnTo = sender
      tablesDao.listAllTables().map(tables => returnTo ! TablesListMessage(tables))
    case UnsubscribeTablesMessage =>
      eventBus.unsubscribe(sender)
    case TestExceptionMessage =>
      throw new IllegalArgumentException("test")
  }

}

object TablesActor {
  def props(tablesDao: TablesDao, eventBus: WebSocketEventBus) = Props(new TablesActor(tablesDao, eventBus))
}
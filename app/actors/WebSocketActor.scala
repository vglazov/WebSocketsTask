package actors

import actors.events.WebSocketEvent
import actors.messages.{SubscribeTablesMessage, TestExceptionMessage, UnsubscribeTablesMessage, _}
import akka.actor.{Actor, ActorRef, Props}
import com.fasterxml.jackson.core.JsonParseException
import dao.{AuthFailure, AuthSuccess, AuthenticationDao, TablesDao}
import models.{AdminUser, User}
import play.api.libs.json.{JsError, JsSuccess, Json}


/**
  * Created on 1/13/2017.
  */
class WebSocketActor(val out: ActorRef, val authenticationActor: ActorRef, val pingPongActor:ActorRef, val tablesActor:ActorRef) extends Actor{

  def receive: Receive = receive(None)

  private def receive(userLoggedIn: Option[User]): Receive = {
    case msgString: String =>
      try {
        val json = Json.parse(msgString)
        json.validate[RequestMessage] match {
          case s: JsSuccess[RequestMessage] =>
              val message = s.get
              if (checkPermissions(message, userLoggedIn)) routeMessage(message) else self ! NotAuthorizedMessage
          case e: JsError =>
            out ! s"Cannot parse message: ${e.toString}"
        }
      } catch {
        case e: JsonParseException =>
          out ! s"Cannot parse json: ${e.getMessage}"
        case e: Throwable =>
          e.printStackTrace()
          out ! "Unexpected error"
      }
    case event: WebSocketEvent => processResponse(event.message)
    case msg: ResponseMessage => processResponse(msg)
    case _ => throw new IllegalArgumentException("Unexpected message")
  }

  private def processResponse(msg: ResponseMessage) = {
    msg match {
      case AuthResultMessage(authResult) =>
        val user = authResult match {
          case AuthSuccess(u) => Some(u)
          case AuthFailure => None
        }
        context.become(receive(user))
      case _ =>
    }
    out ! Json.stringify(msg.toJson)
  }

  private def checkPermissions(message: RequestMessage, user: Option[User]): Boolean = {
    message match {
      case _: AuthenticateMessage => true
      case _: PingMessage => true
      case SubscribeTablesMessage => user.isDefined
      case UnsubscribeTablesMessage => user.isDefined
      case _ => isAdmin(user)
    }
  }

  private def isAdmin(userOpt: Option[User]): Boolean = userOpt match {
    case Some(user) => user.userType == AdminUser
    case None => false
  }

  private def routeMessage(message: RequestMessage) = {
    message match {
      case msg: AuthenticateMessage => authenticationActor ! msg
      case msg: PingMessage => pingPongActor ! msg
      case msg: AddTableMessage => tablesActor ! msg
      case msg: UpdateTableMessage => tablesActor ! msg
      case msg: RemoveTableMessage => tablesActor ! msg
      case TestExceptionMessage => tablesActor ! TestExceptionMessage
      case SubscribeTablesMessage => tablesActor ! SubscribeTablesMessage
      case UnsubscribeTablesMessage => tablesActor ! UnsubscribeTablesMessage
      case _ => throw new IllegalArgumentException("Unexpected message")
    }
  }

}

object WebSocketActor {
  def props(out: ActorRef, authenticationActor: ActorRef, pingPongActor:ActorRef, tablesActor:ActorRef) =
    Props(new WebSocketActor(out, authenticationActor, pingPongActor, tablesActor))
}

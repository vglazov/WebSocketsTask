package controllers

import javax.inject.Inject

import actors.events.WebSocketEventBus
import actors.{AuthenticationActor, PingPongActor, TablesActor, WebSocketActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import dao.AuthenticationDao.HardcodedAuthenticationDao
import dao.TablesDao.InMemoryTablesDao
import play.api._
import play.api.mvc._
import play.api.libs.streams._

class Application @Inject() (implicit system: ActorSystem, materializer: Materializer)  extends Controller {

  private val eventBus = new WebSocketEventBus(system)
  private val authenticationActor: ActorRef = system.actorOf(AuthenticationActor.props(HardcodedAuthenticationDao))
  private val pingPongActor: ActorRef = system.actorOf(PingPongActor.props)
  private val tablesActor: ActorRef = system.actorOf(TablesActor.props(InMemoryTablesDao, eventBus))


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def webSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out, authenticationActor, pingPongActor, tablesActor))
  }

}
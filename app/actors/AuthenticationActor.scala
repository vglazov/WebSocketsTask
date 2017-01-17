package actors

import actors.messages.{AuthResultMessage, AuthenticateMessage}
import akka.actor.{Actor, Props}
import dao.AuthenticationDao

/**
  * Created on 1/13/2017.
  */
class AuthenticationActor(val authDao: AuthenticationDao) extends Actor {

  import context.dispatcher
  override def receive: Receive = {
    case msg: AuthenticateMessage =>
      val returnTo = sender
      authDao.authenticate(msg.username, msg.password) onSuccess {
        case result => returnTo ! AuthResultMessage(result)
      }
  }
}

object AuthenticationActor {
  def props(authenticationDao: AuthenticationDao) = Props(new AuthenticationActor(authenticationDao))
}
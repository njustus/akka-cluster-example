package njustus.clusterexample.typed

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

import scala.util.Random

object SessionHandler extends StatefulActor {
  override type ReceivingMessages = Messages
  override type State = Map[String, ActorRef[AnyRef]]

  sealed trait Messages

  case class Join(sessionId: Option[String], source: ActorRef[AnyRef]) extends Messages

  case class SessionRef[A](sessionId: String, actorRef: ActorRef[A]) extends Messages

  case object SessionNotFound extends Messages

  override protected def zero: Map[String, ActorRef[AnyRef]] = Map.empty

  override def state(sessions: Map[String, ActorRef[AnyRef]]): Behavior[SessionHandler.Messages] = Behaviors.setup { context =>
    context.log.info("started")
    Behaviors.receiveMessagePartial {
      case Join(Some(id), source) =>
        val response = sessions.get(id).map(actorRef => SessionRef(id, actorRef)).getOrElse(SessionNotFound)
        source.tell(response)

        Behaviors.same
      case Join(None, source) =>
        val sessionRef = createSession(context)
        source.tell(sessionRef)

        val newState = sessions + (sessionRef.sessionId -> sessionRef.actorRef.unsafeUpcast)
        context.log.info(s"saving session at ${sessionRef.sessionId}")
        state(newState)
    }
  }

  private def createSession(context: ActorContext[Messages]) = {
    val name = Random.alphanumeric.take(10).map(c => c.toUpper).mkString("")
    context.log.info(s"creating session $name")

    val newSession = context.spawn(Session.newInstance(), name)
    SessionRef(name, newSession)
  }
}

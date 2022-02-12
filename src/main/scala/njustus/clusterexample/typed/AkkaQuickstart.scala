package njustus.clusterexample.typed

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object AkkaQuickstart extends StatelessActor {
  override type ReceivingMessages = AnyRef

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(AkkaQuickstart.newInstance(), "test-system")
  }

  override protected def state(state: Unit): Behavior[ReceivingMessages] = Behaviors.setup { context =>
    //    val handler = context.spawn(SessionHandler.newInstance(), "session-handler")
    val session = context.spawn(Session.newInstance(), "card-game-session-handler")

    //    handler.tell(SessionHandler.Join(None, context.self.unsafeUpcast))
    session.tell(Session.PlayCard(Card("Pik", "5"), context.self))

    Behaviors.receiveMessagePartial {
      case SessionHandler.SessionRef(id, sessionActor) =>
        context.log.info(s"join session $id - actor ${sessionActor.path}")
        Behaviors.same
    }
  }
}

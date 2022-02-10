package njustus.clusterexample

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object AkkaQuickstart extends StatelessActor {
  override type ReceivingMessages = SessionHandler.ReceivingMessages

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(AkkaQuickstart.newInstance(), "test-system")
  }

  override protected def state(state: Unit): Behavior[ReceivingMessages] = Behaviors.setup { context =>
    val handler = context.spawn(SessionHandler.newInstance(), "session-handler")

    handler.tell(SessionHandler.Join(None, context.self.unsafeUpcast))

    Behaviors.receiveMessagePartial {
      case SessionHandler.SessionRef(id, sessionActor) =>
        context.log.info(s"join session $id - actor ${sessionActor.path}")
        Behaviors.same
    }
  }
}

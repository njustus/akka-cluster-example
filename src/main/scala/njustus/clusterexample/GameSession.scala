package njustus.clusterexample

import akka.actor._
import njustus.clusterexample.dtos.GameState

class GameSession extends Actor with ActorLogging {
  private var state = GameState.random
  override def receive: Receive = {
    case x => log.info("unhandled message. state {}",state)
  }
}

object GameSession {
  def props: Props = Props(new GameSession())
}

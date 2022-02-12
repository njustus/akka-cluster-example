package njustus.clusterexample.typed

import akka.actor.typed._
import akka.actor.typed.scaladsl._

import scala.util.Random

case class Card(color: String, symbol: String) {
  override def toString: String = s"$color - $symbol"
}

case class GameState(remainingCards: List[Card], playedCards: List[Card]) {
  def topCard: Card = playedCards.head
}

object Session extends StatefulActor {
  sealed trait Messages

  case class PlayCard(card: Card, player: ActorRef[AnyRef]) extends Messages

  case object DrawCard extends Messages

  override type State = GameState
  override type ReceivingMessages = Messages

  override protected def state(gameState: GameState): Behavior[Messages] = Behaviors.setup { context =>
    Behaviors.receiveMessagePartial {
      case PlayCard(c, player) =>
        context.log.info(s"${player.path.name} is playing ${c}")
        val newState = gameState.copy(playedCards = gameState.playedCards.prepended(c))

        state(newState)
    }

  }

  override protected def zero: GameState = {
    GameState(List.empty, List.empty)
  }
}

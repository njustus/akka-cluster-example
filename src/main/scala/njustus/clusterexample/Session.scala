package njustus.clusterexample

import akka.actor.typed.scaladsl._
import akka.actor.typed._

import scala.util.Random

case class Card(color: String, symbol: String) {
  override def toString: String = s"$color - $symbol"
}

case class GameState(remainingCards:List[Card], playedCards:List[Card]) {
  def topCard: Card = playedCards.head
}

object Session extends StatefulActor {
  sealed trait Messages
  case class PlayCard(card:Card, player: ActorRef[AnyRef]) extends Messages
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
    val cards = List.fill(12)((Random.between(0, 3), Random.between(0, 12)))
      .map { case (color, symbol) => Card(colorByIndex(color), symbolByIndex(symbol)) }

    GameState(cards, List.empty)
  }

  def colorByIndex(idx:Int): String = idx match {
    case 0 => "Pik"
    case 1 => "Karo"
    case 2 => "Herz"
  }

    def symbolByIndex(idx: Int): String = idx match {
      case 12 => "King"
      case 12 => "Queen"
      case 11 => "Prince"
      case _ => idx.toString
    }
}

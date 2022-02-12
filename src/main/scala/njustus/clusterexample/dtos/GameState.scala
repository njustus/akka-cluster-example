package njustus.clusterexample.dtos

import scala.util.Random

case class GameState(remainingCards:List[Card], playedCards:List[Card]) {
  def openCard: Card = playedCards.head
}

object GameState {
  def newShuffledCards: List[Card] = {
    val maxCards = 40
    val symbols = List.fill(maxCards)(Random.between(1, 13))
    val colors = List.fill(maxCards)(Random.between(0, 2)).map {
      case 0 => Blue
      case 1 => Red
    }

    colors.zip(symbols).map { case (color, symbol) =>
      Card(color, symbol.toString)
    }
  }

  def random: GameState = {
    GameState(newShuffledCards, List.empty)
  }
}

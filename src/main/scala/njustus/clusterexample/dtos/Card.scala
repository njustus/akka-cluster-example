package njustus.clusterexample.dtos

sealed trait CardColor
case object Blue extends CardColor
case object Red extends CardColor

case class Card(color: CardColor, symbol: String) {
  override def toString: String = s"$color - $symbol"
}

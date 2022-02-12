package njustus.clusterexample.textedit

import akka.actor.ActorRef

sealed trait TextEditingProtocol extends EditMessage

object TextEditingProtocol {
  case class TextFileUpdate(textFile: TextFile, editSource:ActorRef)  extends TextEditingProtocol
  case class EditLine(lineNo: Int, content : String) extends TextEditingProtocol

  case object Join extends TextEditingProtocol
  case object Leave extends TextEditingProtocol
}

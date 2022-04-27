package njustus.clusterexample.textedit

import akka.actor._
import njustus.clusterexample.textedit.dtos._

import scala.collection.mutable

class TextFileActor(interpreter: TextEditorInterpreter,
                    initialTextFile: TextFile) extends Actor with CommonActor {
  //state
  private var currentTextFile = initialTextFile
  private val editingPeers = mutable.ListBuffer.empty[ActorRef]

  //message handling
  override def receive: Receive = {
    case x:Int => log.error("unhandled")
  }

  private def updateTextFile(newTextFile: TextFile, editPeer: ActorRef): Unit = {
  }
}

object TextFileActor {
  def props(interpreter: TextEditorInterpreter, textFile: TextFile): Props = Props(new TextFileActor(interpreter, textFile))
}

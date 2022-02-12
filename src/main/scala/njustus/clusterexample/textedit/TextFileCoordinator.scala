package njustus.clusterexample.textedit

import akka.actor._
import njustus.clusterexample.textedit.dtos._

import scala.collection.mutable

class TextFileCoordinator(interpreter: TextEditorInterpreter,
                          initialTextFile: TextFile) extends Actor with CommonActor {
  private var currentTextFile = initialTextFile

  private var editingPeers = mutable.ListBuffer.empty[ActorRef]

  override def receive: Receive = {
    case TextEditingProtocol.Join =>
      val peer = sender()
      editingPeers.append(peer)
      log.info("{} joins - {} editors", peer, editingPeers.size)

      peer.tell(TextEditingProtocol.TextFileUpdate(currentTextFile, context.self), context.self)
    case TextEditingProtocol.Leave =>
      val peer = sender()
      val peerIdx = editingPeers.indexOf(peer)
      if(peerIdx > -1) {
        editingPeers.remove(peerIdx)
        log.info("{} leaves - {} editors", peer, editingPeers.size)
      }

    case editLine: TextEditingProtocol.EditLine =>
      val peer = sender()
      val patch = EditPatch(editLine.content, Some(peer.path.name))
      log.info("edit received {}", editLine)

      val newTextFile = interpreter.editLine(currentTextFile, editLine.lineNo, patch)
      updateTextFile(newTextFile, peer)
  }

  private def updateTextFile(newTextFile: TextFile, editPeer:ActorRef): Unit = {
    currentTextFile = newTextFile

    val textFileUpdate = TextEditingProtocol.TextFileUpdate(newTextFile, editPeer)
    log.debug("editing patch applied, notify peers")
    editingPeers.foreach { actor =>
      actor.tell(textFileUpdate, context.self)
    }
  }
}

object TextFileCoordinator {
  def props(interpreter: TextEditorInterpreter, textFile: TextFile): Props = Props(new TextFileCoordinator(interpreter, textFile))
}

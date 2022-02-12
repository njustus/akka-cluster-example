package njustus.clusterexample.textedit

import akka.actor._

import scala.util.Random

class EditingPeer(fileCoordinator: ActorRef) extends Actor with CommonActor {
  override def preStart(): Unit = {
    super.preStart()
    log.info("joining to {}", fileCoordinator.path)
    fileCoordinator.tell(TextEditingProtocol.Join, context.self)
  }

  override def receive: Receive = {
    case update:TextEditingProtocol.TextFileUpdate =>
      log.info("Received initial file {}", update)
      context.become(initialized(update.textFile))
  }

  private def initialized(textFile: TextFile): Receive = {
    case update:TextEditingProtocol.TextFileUpdate =>
      log.debug("Received update {}", update)
      context.become(initialized(update.textFile))
    case EditingPeer.Tick =>
      fileCoordinator.tell(edit(textFile), context.self)
  }

  private def edit(textFile: TextFile): TextEditingProtocol.EditLine = {
    val lineIdx = Random.between(0, textFile.length)
    val newLine = Random.alphanumeric.take(10).mkString("")
    log.debug("going to edit line {} with {}", lineIdx, newLine)

    TextEditingProtocol.EditLine(
      lineIdx,
      newLine
    )
  }
}

object EditingPeer {
  case object Tick extends EditMessage

  def props(fc: ActorRef): Props = Props(new EditingPeer(fc))
}

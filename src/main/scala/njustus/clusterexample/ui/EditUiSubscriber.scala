package njustus.clusterexample.ui

import akka.actor.{ActorRef, Props}
import njustus.clusterexample.textedit.CommonActor
import njustus.clusterexample.textedit.dtos._

private[ui] class EditUiSubscriber(fileCoordinator: ActorRef,
                       indexController: IndexController) extends CommonActor {

  override def preStart(): Unit = {
    super.preStart()
    log.info("joining to {}", fileCoordinator.path)
    fileCoordinator.tell(TextEditingProtocol.Join, context.self)
  }

  override def receive: Receive = {
    case update: TextEditingProtocol.TextFileUpdate =>
      log.info("Received file update {}", update)
      indexController.updateText(update.textFile)
  }
}

private[ui] object EditUiSubscriber {
  def props(fc: ActorRef, idx:IndexController): Props = Props(new EditUiSubscriber(fc, idx))
}

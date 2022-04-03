package njustus.clusterexample

import akka.actor._
import njustus.clusterexample.textedit._
import njustus.clusterexample.textedit.dtos._
import org.slf4j.{Logger, LoggerFactory}

abstract class CommonMain {
  val log: Logger = LoggerFactory.getLogger("main")

  val systemName = "editing-system"

  val file: TextFile = TextFile.fromResource("book.txt")
  val coordinatorName = s"coordinator-${file.fileName}"
  val textFileActorProps: Props = TextFileActor.props(new SimpleEditor(), file)

  def isServer(settings: ActorSystem.Settings): Boolean = {
    settings.config.getString("editing.role").equalsIgnoreCase("SERVER")
  }

  def getServerAddress(settings: ActorSystem.Settings): String = {
    settings.config.getString("editing.server.address")
  }

}

package njustus.clusterexample

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.cluster._
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import njustus.clusterexample.textedit.EditingPeerActor.Tick
import njustus.clusterexample.textedit._
import njustus.clusterexample.textedit.dtos.TextFile
import org.slf4j.{Logger, LoggerFactory}

import java.lang.System.LoggerFinder
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration


abstract class CommonMain {
  val log: Logger = LoggerFactory.getLogger("main")

  val systemName = "editing-system"

  val file: TextFile = TextFile.fromPath(Paths.get("./book.txt"))
  val coordinatorName = s"coordinator-${file.fileName}"
  val coordinatorProps: Props = TextFileCoordinatingActor.props(new SimpleEditor(), file)

  def isServer(settings: ActorSystem.Settings): Boolean = {
    settings.config.getString("editing.role").equalsIgnoreCase("SERVER")
  }

  def getServerAddress(settings: ActorSystem.Settings): String = {
    settings.config.getString("editing.server.address")
  }

}

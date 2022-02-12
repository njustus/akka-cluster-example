package njustus.clusterexample

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster._
import njustus.clusterexample.textedit._

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration

object ClusteredMain extends CommonMain {
  def main(args: Array[String]): Unit = {
    val cluster = Cluster(ActorSystem("editing-system"))
    val system = cluster.system

    if (isServer(system.settings)) {
      log.info("starting server")
      system.actorOf(coordinatorProps, coordinatorName)
    } else {
      log.info("starting clients")
      bootstrapPeer(cluster)
    }
  }

  private def bootstrapPeer(cluster: Cluster): Unit = cluster.registerOnMemberUp {
    val system = cluster.system
    implicit val exec: ExecutionContextExecutor = system.dispatcher

    val actorPath = s"akka://editing-system@${getServerAddress(system.settings)}/user/$coordinatorName"
    val path = system.actorSelection(actorPath).resolveOne(FiniteDuration(5, TimeUnit.MINUTES))

    path.onComplete {
      case scala.util.Success(coordinatorProxy) =>
        log.info("remote path: " + coordinatorProxy.path)

        val peer = system.actorOf(EditingPeerActor.props(coordinatorProxy), "Tim")
        for (_ <- 0 until 5) {
          peer.tell(EditingPeerActor.Tick, ActorRef.noSender)
          Thread.sleep(5000)
        }
      case scala.util.Failure(exception) =>
        log.error(s"coordinator not resolved", exception)
    }
  }
}

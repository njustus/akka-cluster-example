package njustus.clusterexample

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster._
import njustus.clusterexample.textedit._

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.{Duration, FiniteDuration}

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
    val pathFuture = system.actorSelection(actorPath).resolveOne(FiniteDuration(5, TimeUnit.MINUTES))

    val coordinatorProxy = Await.result(pathFuture, Duration.Inf)
    log.info("remote path: " + coordinatorProxy.path)

    val peer = system.actorOf(EditingPeerActor.props(coordinatorProxy), "Tim")
    for (_ <- 0 until 5) {
      peer.tell(EditingPeerActor.Tick, ActorRef.noSender)
      Thread.sleep(5000)
    }
  }
}

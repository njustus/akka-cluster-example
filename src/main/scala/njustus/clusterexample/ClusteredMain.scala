package njustus.clusterexample

import akka.actor.{ActorSystem, PoisonPill}
import akka.cluster._
import njustus.clusterexample.textedit._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{Await, ExecutionContextExecutor}

object ClusteredMain extends CommonMain {
  def main(args: Array[String]): Unit = {
    val cluster = Cluster(ActorSystem("editing-system"))
    val system = cluster.system

    if (isServer(system.settings)) {
      log.info("starting server")
      system.actorOf(textFileActorProps, coordinatorName)
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

    val textFileActor = Await.result(pathFuture, Duration.Inf)
    log.info("remote path: " + textFileActor.path)

    val tim = system.actorOf(EditingPeerActor.props(textFileActor), "Tim")
    val ina = system.actorOf(EditingPeerActor.props(textFileActor), "Ina")
  }
}

package njustus.clusterexample

import akka.actor.{ActorSystem, PoisonPill}
import akka.cluster._
import njustus.clusterexample.textedit._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._

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
    log.info("cluster ready")
    val system = cluster.system
    ???
  }
}

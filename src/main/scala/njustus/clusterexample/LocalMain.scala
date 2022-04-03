package njustus.clusterexample

import akka.actor.{ActorSystem, PoisonPill}
import njustus.clusterexample.textedit._

object LocalMain extends CommonMain {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(systemName)

    val coordinator = system.actorOf(coordinatorProps, coordinatorName)

    val tim = system.actorOf(EditingPeerActor.props(coordinator), "Tim");
    val bob = system.actorOf(EditingPeerActor.props(coordinator), "Bob");
    val ina = system.actorOf(EditingPeerActor.props(coordinator), "Ina");
  }
}

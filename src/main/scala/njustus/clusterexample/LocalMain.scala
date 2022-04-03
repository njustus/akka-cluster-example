package njustus.clusterexample

import akka.actor.{ActorSystem, PoisonPill}
import njustus.clusterexample.textedit._

object LocalMain extends CommonMain {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(systemName)

    val textFileActor = system.actorOf(textFileActorProps, coordinatorName)

    val tim = system.actorOf(EditingPeerActor.props(textFileActor), "Tim");
    val bob = system.actorOf(EditingPeerActor.props(textFileActor), "Bob");
    val ina = system.actorOf(EditingPeerActor.props(textFileActor), "Ina");
  }
}

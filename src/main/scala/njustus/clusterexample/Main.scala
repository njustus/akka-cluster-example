package njustus.clusterexample

import akka.actor.{ActorRef, ActorSystem}
import njustus.clusterexample.textedit._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends CommonMain {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(systemName)

    val coordinator = system.actorOf(coordinatorProps, coordinatorName)

    val peer1 = system.actorOf(EditingPeer.props(coordinator), "Tim");
    val peer2 = system.actorOf(EditingPeer.props(coordinator), "Tom");
    val peer3 = system.actorOf(EditingPeer.props(coordinator), "Jenny");

    for(_ <- 0 until 5) {
      peer1.tell(EditingPeer.Tick, ActorRef.noSender)
      Thread.sleep(10000)
      peer3.tell(EditingPeer.Tick, ActorRef.noSender)
    }

    peer2.tell(EditingPeer.Tick, ActorRef.noSender)

    Thread.sleep(3000)
    Await.result(system.terminate(), Duration.Inf)
  }

}
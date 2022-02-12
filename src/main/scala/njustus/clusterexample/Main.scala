package njustus.clusterexample

import akka.actor.{ActorRef, ActorSystem}
import njustus.clusterexample.textedit._

import java.nio.file.Paths
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {

  def main(args: Array[String]): Unit = {
    val file = TextFile.fromPath(Paths.get("./book.txt"))

    val system = ActorSystem("test-system")

    val coordinator = system.actorOf(TextFileCoordinator.props(new SimpleEditor(), file), s"coordinator-${file.fileName}")

    val peer1 = system.actorOf(EditingPeer.props(coordinator), "peer-0");
    val peer2 = system.actorOf(EditingPeer.props(coordinator), "peer-1");

    for(_ <- 0 until 5) {
      peer1.tell(EditingPeer.Tick, ActorRef.noSender)
      Thread.sleep(10000)
    }

    peer2.tell(EditingPeer.Tick, ActorRef.noSender)

    Thread.sleep(3000)
    Await.result(system.terminate(), Duration.Inf)
  }

}

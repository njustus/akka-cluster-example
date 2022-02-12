package njustus.clusterexample

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class MyActor extends Actor with ActorLogging {
  override def receive: Receive = ???
}

object MyActor {
  def props: Props = Props(new MyActor())
}

object Main {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("test-system")

    val session = system.actorOf(GameSession.props, "game-session")
    session ! 5
    session ! 5
    session ! 5
  }

}

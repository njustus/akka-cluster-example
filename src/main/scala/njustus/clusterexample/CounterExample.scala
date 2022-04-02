package njustus.clusterexample

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Counter(seed:Int) extends Actor with ActorLogging {
  private var count:Int = seed

  override def receive: Receive = {
    case x:Any =>
      count += 1
      log.info(s"received $x incremented counter to $count")
  }
}

object CounterExample {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("test-system")

    val counterRef:ActorRef = system.actorOf(
      Props(classOf[Counter], 5),
      "my-counter")

    counterRef ! 1
    counterRef ! "test"
    counterRef.tell("test2", ActorRef.noSender)

    Await.result(system.terminate(), Duration.Inf)
  }
}

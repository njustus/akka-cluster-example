package njustus.clusterexample.textedit

import akka.actor.{Actor, ActorLogging}

trait CommonActor extends Actor with ActorLogging {
  override def preStart(): Unit = {
    log.info("started")
  }

  override def postStop(): Unit = {
    log.info("stopped")
  }

}

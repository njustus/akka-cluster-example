package njustus.clusterexample.textedit

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext

trait CommonActor extends Actor with ActorLogging {
  protected implicit val dispatcher:ExecutionContext = context.dispatcher

  override def preStart(): Unit = {
    log.info("started")
  }

  override def postStop(): Unit = {
    log.info("stopped")
  }

}

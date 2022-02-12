package njustus.clusterexample.typed

import akka.actor.typed.Behavior

trait StatefulActor {
  type ReceivingMessages
  type State

  def newInstance(): Behavior[ReceivingMessages] = {
    state(zero)
  }

  protected def state(state: State): Behavior[ReceivingMessages]

  protected def zero: State
}

trait StatelessActor extends StatefulActor {
  override type State = Unit

  override protected def zero: State = ()
}

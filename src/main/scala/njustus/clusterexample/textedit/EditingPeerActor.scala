package njustus.clusterexample.textedit

import akka.actor._
import com.github.javafaker.Faker
import njustus.clusterexample.textedit.dtos._

import java.util.Locale
import scala.util.Random
import scala.concurrent.duration._

class EditingPeerActor(textFileActor: ActorRef) extends Actor with CommonActor {

  private val faker = new Faker(Locale.GERMANY)

  override def preStart(): Unit = {
    super.preStart()
    log.info("joining to {}", textFileActor.path)
    textFileActor.tell(TextEditingProtocol.Join, context.self)

    context.system.scheduler.scheduleAtFixedRate(2 seconds, 5 seconds, self, EditingPeerActor.Tick)
  }

  override def receive: Receive = {
    case update: TextEditingProtocol.TextFileUpdate =>
      log.info("Received initial file {}", update)
      context.become(initialized(update.textFile))
  }

  private def initialized(textFile: TextFile): Receive = {
    case update: TextEditingProtocol.TextFileUpdate =>
      log.debug("Received update {}", update)
      context.become(initialized(update.textFile))

    case EditingPeerActor.Tick =>
      textFileActor.tell(edit(textFile), context.self)
  }

  private def edit(textFile: TextFile): TextEditingProtocol.EditLine = {
    val newLine = generateNewLine()
    val lineIdx = Random.between(0, textFile.length + 1)
    log.debug("going to edit line {} with {}", lineIdx, newLine)

    TextEditingProtocol.EditLine(
      lineIdx,
      newLine
    )
  }

  private def generateNewLine(): String = {
    if (Random.nextFloat() > 0.8)
      faker.yoda().quote()
    else
      faker.shakespeare().romeoAndJulietQuote()
  }
}

object EditingPeerActor {
  case object Tick

  def props(fc: ActorRef): Props = Props(new EditingPeerActor(fc))
}

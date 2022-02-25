package njustus.clusterexample.ui

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import njustus.clusterexample.textedit.dtos.TextFile
import org.slf4j.LoggerFactory

import java.net.URL
import java.util.ResourceBundle
import scala.jdk.CollectionConverters._

private[ui] class IndexController extends Initializable {
  private val log = LoggerFactory.getLogger(classOf[IndexController])

  @FXML
  private var centerBox: VBox = null

  @FXML
  private var startBtn: Button = null

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    log.info("initialized")
  }

  @FXML
  def startClicked(event: ActionEvent): Unit = {
    println("start clicked")
  }

  def updateText(textFile: TextFile): Unit = Platform.runLater { () =>
    log.debug("updating text..")
    if (centerBox.getChildren.isEmpty || textFile.lines.size > centerBox.getChildren.size) {
      log.debug("initializing {} components", textFile.lines)
      centerBox.getChildren.clear()
      val components = textFile.lines.map { patch =>
        val c = new LineComponent()
        c.update(patch)
        c
      }

      centerBox.getChildren.addAll(components.asJava)
    } else {
      centerBox.getChildren.asScala.zip(textFile.lines).foreach {
        case (lbl: LineComponent, patch) =>
          log.debug(s"updating patch ${patch.content}")
          lbl.update(patch)
      }
    }
  }
}

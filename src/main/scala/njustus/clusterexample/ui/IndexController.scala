package njustus.clusterexample.ui

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory

import java.net.URL
import java.util.ResourceBundle

class IndexController extends Initializable {
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

}

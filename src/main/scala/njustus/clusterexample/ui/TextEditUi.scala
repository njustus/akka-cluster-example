package njustus.clusterexample.ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

class TextEditUi extends Application {

  override def start(primaryStage: Stage): Unit = {
    val loader = new FXMLLoader(getClass.getClassLoader.getResource("index.fxml"))
    val mainView: Pane = loader.load()
    val indexController: IndexController = loader.getController

    val scene = new Scene(mainView, 600, 400)
    primaryStage.setScene(scene)

    primaryStage.setTitle("TextEdit")
    primaryStage.setOnCloseRequest(ev => {
    })
    primaryStage.show()
  }

}

object TextEditUi {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[TextEditUi], args:_*)
  }
}

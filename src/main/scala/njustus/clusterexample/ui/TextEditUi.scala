package njustus.clusterexample.ui

import akka.actor.ActorSystem
import akka.cluster.Cluster
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import njustus.clusterexample.LocalMain.{coordinatorName, getServerAddress}
import njustus.clusterexample.textedit.dtos.TextFile
import org.slf4j.{Logger, LoggerFactory}

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._

class TextEditUi extends Application {
  val log: Logger = LoggerFactory.getLogger(TextEditUi.getClass)

  override def start(primaryStage: Stage): Unit = {
    val loader = new FXMLLoader(getClass.getClassLoader.getResource("index.fxml"))
    val mainView: Pane = loader.load()
    val indexController: IndexController = loader.getController

    val scene = new Scene(mainView, 600, 400)
    scene.getStylesheets.add("/styles.css")
    primaryStage.setScene(scene)

    primaryStage.setTitle("TextEdit")

    val cluster = Cluster(ActorSystem("editing-system"))
    val system = cluster.system
    implicit val executor: ExecutionContextExecutor = system.dispatcher

    primaryStage.setOnCloseRequest(ev => {
      Await.result(system.terminate(), 5 seconds)
    })
    primaryStage.show()

    cluster.registerOnMemberUp {
      val actorPath = s"akka://editing-system@${getServerAddress(system.settings)}/user/$coordinatorName"
      val path = system.actorSelection(actorPath).resolveOne(FiniteDuration(5, TimeUnit.MINUTES))

      path.onComplete {
        case scala.util.Success(coordinatorProxy) =>
          log.info("remote path: " + coordinatorProxy.path)

          system.actorOf(EditUiSubscriber.props(coordinatorProxy, indexController))
        case scala.util.Failure(exception) =>
          log.error(s"coordinator not resolved", exception)
      }
    }
  }
}

object TextEditUi {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[TextEditUi], args:_*)
  }
}

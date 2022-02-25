package njustus.clusterexample.ui

import javafx.animation.FadeTransition
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.util.Duration
import njustus.clusterexample.textedit.dtos.EditPatch

private[ui] class LineComponent extends HBox {
  private val text = new Label()
  private val editor = new Label()
  private val ft = new FadeTransition(Duration.seconds(2), text)
  ft.setFromValue(0.5)
  ft.setToValue(1.0)

  super.getStyleClass.add("line-component")
  editor.getStyleClass.add("line-component-editor")
  text.getStyleClass.add("line-component-text")

  super.getChildren.addAll(editor, text)

  def update(patch: EditPatch): Unit = {
    if(patch.content != text.getText) {
      ft.playFromStart()
    }
    text.setText(patch.content)
    editor.setText(s"[${patch.editor}]:")
  }
}

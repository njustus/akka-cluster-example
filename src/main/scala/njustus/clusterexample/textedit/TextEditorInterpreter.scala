package njustus.clusterexample.textedit

import java.time.LocalDateTime

trait TextEditorInterpreter {
  def editLine(text:TextFile, lineNo:Int, patch: String): TextFile
}

class SimpleEditor extends TextEditorInterpreter {
  override def editLine(text:TextFile, lineNo:Int, patch: String): TextFile = {
    if(lineNo<0 || lineNo>text.lines.length) {
      text
    } else {
      val (start, rest) = text.lines.splitAt(lineNo)
      val patchedLines = start.appendedAll(patch::rest.tail)

      TextFile(text.path, patchedLines, LocalDateTime.now())
    }
  }
}

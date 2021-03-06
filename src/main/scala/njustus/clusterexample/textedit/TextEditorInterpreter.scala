package njustus.clusterexample.textedit

import njustus.clusterexample.textedit.dtos._

import java.time.LocalDateTime

trait TextEditorInterpreter {
  def editLine(text: TextFile, lineNo: Int, patch: EditPatch): TextFile
}

class SimpleEditor extends TextEditorInterpreter {
  override def editLine(text: TextFile, lineNo: Int, patch: EditPatch): TextFile = {
    if (lineNo == text.lines.length) {
      TextFile(text.fileName, text.lines.appended(patch), LocalDateTime.now())
    } else if (lineNo < 0 || lineNo > text.lines.length) {
      text
    } else {
      val (start, rest) = text.lines.splitAt(lineNo)
      val patchedLines = start.appendedAll(patch :: rest.tail)

      TextFile(text.fileName, patchedLines, LocalDateTime.now())
    }
  }
}

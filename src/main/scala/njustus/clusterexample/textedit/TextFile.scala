package njustus.clusterexample.textedit

import java.nio.file.{Files, Path}
import java.time.LocalDateTime
import scala.jdk.CollectionConverters._

case class EditPatch(content: String,
                     editorName: Option[String]) {
  def editor: String = editorName.getOrElse("unknown")

  override def toString: String = s"${content} (${editor})"
}

case class TextFile(path: Path,
                    lines: List[EditPatch],
                    lastUpdate:LocalDateTime) {
  def length: Int = lines.length

  def fileName: String = path.getFileName.toString

  override def toString: String = {
    s"${path.getFileName} (${lastUpdate})\n"+
      s"${lines.mkString("\n")}"
  }
}

object TextFile {

  def fromPath(path: Path): TextFile = {
    val lines = Files.readAllLines(path).asScala.toList.map { line =>
      EditPatch(line, None)
    }

    TextFile(path, lines, LocalDateTime.now())
  }
}

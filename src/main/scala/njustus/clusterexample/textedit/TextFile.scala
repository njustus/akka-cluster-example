package njustus.clusterexample.textedit

import java.nio.file.{Files, Path}
import java.time.LocalDateTime
import scala.jdk.CollectionConverters._

case class TextFile(path: Path,
                    lines: List[String],
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
    val lines = Files.readAllLines(path).asScala.toList
    TextFile(path, lines, LocalDateTime.now())
  }
}

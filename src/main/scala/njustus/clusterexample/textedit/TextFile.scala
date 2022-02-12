package njustus.clusterexample.textedit

import java.nio.file.{Files, Path}
import java.time.LocalDateTime
import scala.jdk.CollectionConverters._

case class TextFile(path: Path,
                    lines: List[String],
                    lastUpdate:LocalDateTime) {
  def length: Int = lines.length

  def fileName: String = path.getFileName.toString
}

object TextFile {
  def fromPath(path: Path): TextFile = {
    val lines = Files.readAllLines(path).asScala.toList
    TextFile(path, lines, LocalDateTime.now())
  }
}

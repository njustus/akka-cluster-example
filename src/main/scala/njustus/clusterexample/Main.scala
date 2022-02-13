package njustus.clusterexample

import njustus.clusterexample.ui.TextEditUi

object Main {
  def main(args: Array[String]): Unit = {
    if(args.contains("remote")) {
      println("starting clustered system")
      ClusteredMain.main(args)
    } else if(args.contains("ui")) {
      TextEditUi.main(args)
    } else {
      println("starting local system")
      LocalMain.main(args)
    }
  }
}

package njustus.clusterexample

import njustus.clusterexample.ui.TextEditUi

object Main {
  def main(args: Array[String]): Unit = {
    val role = System.getenv("ROLE")
    if(role != null && (role.equalsIgnoreCase("PEER") || role.equalsIgnoreCase("SERVER"))) {
      println("starting clustered system")
      ClusteredMain.main(args)
    } else if(role != null && role.equalsIgnoreCase("UI")) {
      println("starting ui for clustered system")
      TextEditUi.main(args)
    } else {
      println("starting local system")
      LocalMain.main(args)
    }
  }
}

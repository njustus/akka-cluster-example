package njustus.clusterexample

object Main {
  def main(args: Array[String]): Unit = {
    if(args.contains("remote")) {
      println("starting clustered system")
      ClusteredMain.main(args)
    } else {
      println("starting local system")
      LocalMain.main(args)
    }
  }
}

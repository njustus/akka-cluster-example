package njustus.clusterexample

object Main {
  def main(args: Array[String]): Unit = {
    if(args.contains("remote")) {
      ClusteredMain.main(args)
    } else {
      LocalMain.main(args)
    }
  }
}

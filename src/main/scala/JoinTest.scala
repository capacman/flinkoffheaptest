/**
  * Created by capacman on 17.06.2016.
  */
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._

object JoinTest {
  def main(args: Array[String]) {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val rootPath = "/home/capacman/Data/wiki/"
    val wikidata = env.readTextFile(s"${rootPath}wikidata.tsv").name("Read Data").mapPartition {
      lineIter =>
        lineIter.map { line =>
          val values = line.split("\t")
          (
            if (values(0).isEmpty) None else Some(Integer.parseInt(values(0))),
            Integer.parseInt(values(1)),
            Integer.parseInt(values(2)),
            values(3),
            if (values.length < 5) null else values(4)
            )
        }
    }.name("Parse Data")

    val idtitle = env.readTextFile(s"${rootPath}idtitle.tsv").name("Read idtitle").mapPartition {
      lineIter =>
        lineIter.map { line =>
          val values = line.split("\t")
          (Integer.parseInt(values(0)), values(1))
        }
    }.name("Parse idtitle")

    val leftPrepared = wikidata.partitionByHash(1).sortPartition(1, Order.ASCENDING).name("sortingLeft")
    val rightPrepared = idtitle.partitionByHash(0).sortPartition(0, Order.ASCENDING).name("sortingRight")
    val result = leftPrepared.join(rightPrepared).where(1).equalTo(0).map(_ => 1l).reduce(_ + _)
    result.printOnTaskManager("output:")
    println(env.getExecutionPlan())
    env.execute()
  }
}

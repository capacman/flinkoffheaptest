name := "flinkoffheap"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val flinkVersion: String = "1.0.3"
  val log4jExclusions = Seq(ExclusionRule(organization = "log4j"), ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12"))
  Seq("org.apache.flink" %% "flink-clients" % flinkVersion excludeAll (log4jExclusions: _*),
    "org.apache.flink" %% "flink-scala" % flinkVersion excludeAll (log4jExclusions: _*),
    "org.apache.flink" %% "flink-streaming-scala" % flinkVersion excludeAll (log4jExclusions: _*),
    "org.apache.flink" % "flink-batch-connectors" % flinkVersion excludeAll (log4jExclusions: _*),
    "ch.qos.logback" % "logback-core" % "1.1.3",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )
}
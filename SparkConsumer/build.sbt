name := "StreamHandler"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "3.0.3" % "provided",
	"org.apache.spark" %% "spark-sql" % "3.0.3" % "provided",
	"com.datastax.spark" %% "spark-cassandra-connector" % "2.4.3",
	"com.datastax.cassandra" % "cassandra-driver-core" % "4.0.0"
)
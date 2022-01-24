import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.types._
import org.apache.spark.sql.cassandra._

import com.datastax.oss.driver.api.core.uuid.Uuids
import com.datastax.spark.connector._

case class DeviceData(device: String, temp: Double, humd: Double, pres: Double)

object StreamHandler {
	def main(args: Array[String]){
		val spark = SparkSession
			.builder
			.appName("Stream Handler")
			.config("spark.cassandra.connection.host", "localhost")
			.getOrCreate()

		import spark.implicits._

		val inputDF = spark
			.readStream
			.format("kafka")
			.option("kafka.bootstrap.servers", "localhost:9092")
			.option("subscribe", "weather")
			.load()

		val rawDF = inputDF.selectExpr("CAST(value AS STRING)").as[String]

		val expandedDF = rawDF.map(row => row.split(','))
			.map(row => DeviceData(
				row(1),
				row(2).toDouble,
				row(3).toDouble,
				row(4).toDouble
			))

		val summaryDF = expandedDF
			.groupBy("device")
			.agg(avg("temp"), avg("humd"), avg("pres"))

		val makeUUID = udf(() => Uuids.timeBased().toString)

		val summaryWithId = summaryDF.withColumn("uuid", makeUUID())
		    .withColumnRenamed("avg(temp)", "temp")
		    .withColumnRenamed("avg(humd)", "humd")
		    .withColumnRenamed("avg(pres)", "pres")

		val query = summaryWithId
			.writeStream
			.trigger(Trigger.ProcessingTime("5 seconds"))
			.foreachBatch{ (batchDF: DataFrame, batchID: Long) => 
				println(s"Writing to Cassandra: $batchID")
				batchDF.write
				  .cassandraFormat("weather", "weather_keyspace")
				  .mode("append")
				  .save()
			
			}
			.outputMode("update")
			.start()

		query.awaitTermination()
	}
}
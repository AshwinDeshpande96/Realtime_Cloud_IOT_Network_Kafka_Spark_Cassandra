
import com.datastax.oss.driver.api.core.CqlSession

import scala.jdk.CollectionConverters._

object WeatherReader {

  def main(args: Array[String]): Unit = {
    val resultSet = session.execute("select * from weather_keyspace.weather");
    val rows = resultSet.all().asScala;
    rows.foreach({ println } );

    println("Weather table has following rows:\nuuid,device,temp,humd,pres\n");
    for (row <- rows) println("%s,%s,%d,%d,%d" format (row.getUUID("uuid").as[String], row.getUUID("device"), row.getUUID("temp"), row.getUUID("humd"), row.getUUID("pres")));

    System.exit(0);
  }

  private val session = CqlSession.builder.build()
}

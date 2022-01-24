# Realtime_Cloud_IOT_Network_Kafka_Spark_Cassandra
AWS Implementation of a realtime iot data processing system that is scalable to user requests

This project simulates a data streaming pipeline. The goal is to connect a source which produce data and send it to a database in real time. To enable scalability in terms of traffic we employ a event-driven streaming service called ***Apache Kafka*** and further onto ***Apache Spark*** to consume, process and send data for storage in a ***Cassandra*** No-SQL Database.

For this project the data source is a simulated IOT device that produce mock data such as temperature, humidity and presssure in real time. Since there could be a number of IOT devices that are added or removed it is important to provide scalability such that a new IOT device can be easily installed without having to make any changes to the pipeline.
(Each IOT device is currently an EC2 Instance. For security measure Apache MSK's inbound rules are configured to accept data from a pre-specified security group)
* Each IOT device publishes to a Kafka Broker in its subnet which consists a Kafka Topic called ***weather***.
* Another EC2 instance in the VPC is employed to host Apache Spark. Here we have Scala program that ingests all data published in the topic ***weather*** in batches. We perform simple processing steps to clean and format our data to be stored in a database.
* A Cassandra Cluster is created which consists of a Keyspace named ***weather_keyspace***. Apache Spark authenticates acceess with Apache Cassandra using SigV4 service.
  * All data is stored in a table called "weather" with columns uuid, device, temp, humd, pres.

## Project Architecture
![Network Architecture](https://github.com/AshwinDeshpande96/Realtime_Cloud_IOT_Network_Kafka_Spark_Cassandra/blob/main/Realtime_IOT.jpg)


## IOT Device
IOT device is a [python file](https://github.com/AshwinDeshpande96/Realtime_Cloud_IOT_Network_Kafka_Spark_Cassandra/iot_devices.py) that produces random temperature, humidity and pressure data with its timestamp. 

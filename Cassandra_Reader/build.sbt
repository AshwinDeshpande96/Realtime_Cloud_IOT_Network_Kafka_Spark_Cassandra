name := "Cassandra Reader"
version := "1.0"
scalaVersion := "2.12.10"
libraryDependencies += "software.aws.mcs" % "aws-sigv4-auth-cassandra-java-driver-plugin" % "4.0.3"
libraryDependencies += "com.datastax.oss" % "java-driver-core" % "4.8.0"
trapExit := false

name := "dynamodb-streaming"
spName := "datastream/dynamodb-streaming"

version := "1.0"

sparkVersion := "2.3.3"

scalaVersion := "2.11.0"

sparkComponents ++= Seq("streaming")

libraryDependencies ++= Seq(
	  "com.thoughtworks.paranamer" % "paranamer" % "2.8",
      "com.amazonaws" % "aws-java-sdk" % "1.11.375",
      "com.amazonaws" % "dynamodb-streams-kinesis-adapter" % "1.4.0",
      "org.apache.spark" %% "spark-streaming-kinesis-asl" % "2.3.3",
      "org.apache.logging.log4j" % "log4j-core" % "2.7",
      "org.apache.logging.log4j" % "log4j-api" % "2.7"
)

spShortDescription := "PySpark DynamoDB Streaming" // Your one line description of your package


spDescription := """PySpark DynamoDB Streaming.
                    |Stream data from DynamoDB streaming source""".stripMargin

spHomepage := "https://bitbucket.org/kenanberbic/apache.spark.dynamodb.streaming"

credentials += Credentials(Path.userHome / ".ivy2" / ".sbtcredentials")
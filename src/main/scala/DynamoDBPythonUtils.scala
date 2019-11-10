package org.apache.spark.streaming.kinesis.DynamoDBStream

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Seconds
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import org.apache.spark.streaming.api.java.JavaStreamingContext
import org.apache.spark.streaming.api.java.JavaDStream
import org.apache.spark.annotation.InterfaceStability

object DynamoDBPythonUtils{
  def _getInitialPositionInStream: Int => InitialPositionInStream = (initialPositionInt: Int) => {
      var initialPosition: InitialPositionInStream = InitialPositionInStream.LATEST
      initialPositionInt match {
        case 0 => initialPosition=InitialPositionInStream.LATEST
        case 1 => initialPosition=InitialPositionInStream.TRIM_HORIZON
        case 2 => initialPosition=InitialPositionInStream.AT_TIMESTAMP
      }
      initialPosition
  }
  
  def ReadRddStream(){
    
  }
  
  def DynamoDBStream(jssc: JavaStreamingContext, streamArn: String, checkpointAppName: String, awsRegion: String,
                  awsKey: String, awsSecret: String, initialPosition: Int, 
                  storageLevel: StorageLevel, maxRecord: Int, checkpointInterval: Int): JavaDStream[Array[Byte]] = {
    
    val stream = DynamoInputDStream.builder
    .streamingContext(jssc)
    .streamArn(streamArn)
    .kinesisCredentials(SparkAWSCredentials.builder.basicCredentials(awsKey, awsSecret).build())
    .regionName(awsRegion)
    .initialPosition(_getInitialPositionInStream(initialPosition))
    .checkpointAppName(checkpointAppName)
    .checkpointInterval(Seconds(checkpointInterval))
    .maxRecords(maxRecord)
    .taskBackoffTimeMillis(500)
    .storageLevel(storageLevel)
    .build();
     
     val jsStream = new JavaDStream(stream)
     jsStream
  }
}
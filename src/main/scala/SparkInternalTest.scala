/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils
import com.amazonaws.services.dynamodbv2.streamsadapter.model.RecordAdapter
import com.amazonaws.services.kinesis.model.Record
import javax.xml.datatype.Duration
import com.google.gson.Gson
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kinesis.DynamoDBStream._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream


object SparkInternalTest {
         //creates an array of strings from raw byte array
  def rawRecordHandler: Record => Array[String] = (record: Record) => 
    new String(record.getData.array()).split(",")

  //converts records to map of attribute value pair
  def recordHandler: Record => String = (record: Record) => {
    val sRecord = record.asInstanceOf[RecordAdapter].getInternalObject
    val jsonText:String = new Gson().toJson(sRecord.getDynamodb.getNewImage)
    jsonText
  }

  //case class that can represent your schema
  case class MyClass(file:String,amount:Int,dummyValue:String)
  
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName("DynamoDB Streaming")
    val ssc = new StreamingContext(conf, Seconds(10))
    val stream = DynamoInputDStream.builder
    .streamingContext(ssc)
    .streamArn("arn:aws:dynamodb:eu-central-1:679734240487:table/p.requests/stream/2019-06-21T09:52:46.259")
    .kinesisCredentials(SparkAWSCredentials.builder.basicCredentials("key", "secret").build())
    .regionName("us-west-1")
    .initialPosition(InitialPositionInStream.TRIM_HORIZON)
    .checkpointAppName("p.requests_streams")
    .checkpointInterval(Seconds(10))
    .maxRecords(1000)
    .taskBackoffTimeMillis(500)
    .storageLevel(StorageLevel.MEMORY_AND_DISK_2)
    .buildWithMessageHandler(recordHandler);

    stream.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
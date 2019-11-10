from py4j.java_gateway import java_import, JavaGateway, JavaObject
from pyspark.serializers import PairDeserializer, NoOpSerializer
from pyspark.streaming import DStream

class DynamoDBStream(object):
    @staticmethod
    def createStream(ssc, arn, destination, awsRegion, awsKey, awsKecret, initialPosition, storageLevel, maxRecord, interval):
        sc = ssc.sparkContext
        java_import(sc._gateway.jvm, "org.apache.spark.streaming.kinesis.DynamoDBStream.DynamoDBPythonUtils")
        DynamoDBPythonUtils = sc._gateway.jvm.DynamoDBPythonUtils

        jlevel = ssc._sc._getJavaStorageLevel(storageLevel)
        jstream = DynamoDBPythonUtils.DynamoDBStream(ssc._jssc,
                                                     arn,
                                                     destination,
                                                     awsRegion,
                                                     awsKey,
                                                     awsKecret,
                                                     initialPosition,
                                                     jlevel,
                                                     maxRecord,
                                                     interval)

        stream = DStream(jstream, ssc, NoOpSerializer())
        return stream.map(lambda record: record.decode("utf-8") if record is not None else None)

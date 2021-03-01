package com.utils

import org.apache.spark.sql.SparkSession

//create spark session (default params appName and master local[*] / with spark submit choose another mode)
object SparkJob {

  //Setup HADOOP_HOME environment variable programmatically
  //System.setProperty("hadoop.home.dir", "C:\\winutils");

  val spark = SparkSession
    .builder()
   // .appName("AppProject")
    .master("local[*]")
    .getOrCreate()

  //set log level error
  spark.sparkContext.setLogLevel("ERROR")
  spark.conf.set("spark.sql.shuffle.partitions",8)

}

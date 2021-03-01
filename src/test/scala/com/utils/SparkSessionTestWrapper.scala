package com.utils

import org.apache.spark.sql.SparkSession

trait SparkSessionTestWrapper {
  lazy implicit val _spark: SparkSession = SparkSession.builder().appName("spark testing session")
    .master("local")
    .getOrCreate()

}

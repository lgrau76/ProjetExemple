package com.project1.impl

import org.apache.spark.sql.{Dataset, SparkSession}


case class PrecipitationSample(
                                Station: String,
                                StationName: String,
                                Elevation: Double,
                                Latitude: Double,
                                Longitude: Double,
                                Date: String,
                                Hpcp: Int,
                                MeasurementFlag: String,
                                QualityFlag: String
                              )

object ExtractProcess {

  def convertToDataset(csvData: String)(implicit spark: SparkSession): Dataset[PrecipitationSample] = {
    import spark.implicits._

    //create Dataset[PrecipitationSample with pre DataFrame columns order forces
    spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(csvData)
      .toDF("Station","StationName","Elevation","Latitude","Longitude","Date","Hpcp","MeasurementFlag","QualityFlag")
      .as[PrecipitationSample]
  }

}

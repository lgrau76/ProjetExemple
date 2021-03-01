package com.project3.impl

import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

//ip,date,time,zone,cik,accession,extention,code,size,idx,norefer,noagent,find,crawler,browser
case class Access(
                                ip: String,
                                date: java.sql.Timestamp,
                                time: String,
                                zone:Double,
                                cik: Double,
                                accession: String,
                                url: String,
                                code: Double,
                                size: Double,
                                idx: Double,
                                norefer: Double,
                                noagent: Double,
                                find: Double,
                                crawler: Double,
                                browser: String
                              )

object Report {

  @transient lazy val log = org.apache.log4j.LogManager.getLogger(Report.getClass)


  //udf functiun to replace Month str by no
  import org.apache.spark.sql.functions.udf
  val replaceMonthStrColByNo = udf((s: String) =>
     s match {
      case "Jan"  => "01"
      case "Feb"  => "02"
      case "Mar"  => "03"
      case "Apr"  => "04"
      case "May"  => "05"
      case "Jun"  => "06"
      case "Jul"  => "07"
      case "Aug"  => "08"
      case "Sep"  => "09"
      case "Oct"  => "10"
      case "Nov"  => "11"
      case "Dec"  => "12"
      case _      => "06" //default arbitrary choose June
    }
  )


 // def getStructuredLog(gzPath: String)(implicit spark: SparkSession):Unit={
 def getStructuredLog(dfsrc: DataFrame)(implicit spark: SparkSession):DataFrame={
    try {
      import org.apache.spark.sql.functions._

      val patternIP = "([0-9]+.[0-9]+.[0-9]+.[0-9]+)"
      val patternDATE = "([0-9]+/(.*)+/[0-9])"//[0-9]+:[0-9]+:[0-9]+:[0-9]+)"
      val patternURL = "([\"]+[GET|POST|PUT|DELETE]+(.*HTTP))"

      val df = dfsrc
        .withColumn("ip", regexp_extract(col("value"), patternIP, 1))
        .withColumn("dateTmp", regexp_extract(col("value"), patternDATE, 1).substr(0,11))
          .withColumn("day", col("dateTmp").substr(0,2))
          .withColumn("month", replaceMonthStrColByNo(col("dateTmp").substr(4,3)))
          .withColumn("year", col("dateTmp").substr(8,4))
          .withColumn("date", to_timestamp(concat_ws("-", col("year"), col("month"), col("day"))))
          .withColumn("unix_ts" , unix_timestamp(col("date")))
        .withColumn("url", regexp_extract(col("value"), patternURL, 1))
        .drop(col("value"))
          .drop(col("dateTmp"))
          .drop(col("day"))
          .drop(col("month"))
          .drop(col("year"))

      //find all the dates having too big number of connection (> 20000) groupby unix_ts + filter + join
      val dfTmp = df
                    .drop(col("ip"))
                    .drop(col("date"))
                    .drop(col("url"))
      val dfFilter20000 = dfTmp
        .groupBy("unix_ts")
        .count()
        .filter(col("count") >= 2)//20000)

      df.join(dfFilter20000,df("unix_ts") ===  dfFilter20000("unix_ts"),"inner")
    }
    catch {
      case e: (Exception)  => {
        log.error(s"error " + this.getClass + "def createReport "+e.getStackTrace)
        throw e
      }
    }
  }

  //gzPath is the file access.log.gz
  def createReportDstiLogs(gzPath: String, outputPath: String, nbMax:Int)(implicit spark: SparkSession):Boolean={

    try {
      import org.apache.spark.sql.functions._
      val df = getStructuredLog(spark.read.text(gzPath).toDF())
      df.printSchema()
      df.show

      //all the dates having too big number of connection (> nbMax)
      val pathReport = outputPath+"Report_Global"

      //create aggregated report
      val dfnbByIp = df.groupBy("ip").count()
        .withColumn("type", lit("ip_count"))
        .withColumn("index", monotonically_increasing_id())
        .withColumn("values", to_json(struct(col("ip"),col("count"))))
        .drop("ip")
        .drop("count")
      dfnbByIp.show()

      val dfnbUrlByDate = df.groupBy("date", "url").count()
        .withColumn("type", lit("date_url_count"))
        .withColumn("index", monotonically_increasing_id())
        .withColumn("values", to_json(struct(col("date"),col("url"),col("count"))))
        .drop("date")
        .drop("url")
        .drop("count")
      dfnbUrlByDate.show()

      val dfnbIpByDate = df.groupBy("date", "ip").count()
        .withColumn("type", lit("date_ip_count"))
        .withColumn("index", monotonically_increasing_id())
        .withColumn("values", to_json(struct(col("date"),col("ip"),col("count"))))
        .drop("date")
        .drop("ip")
        .drop("count")
      dfnbIpByDate.show()

      val dfGlobalReport = dfnbByIp.union(dfnbUrlByDate).union(dfnbIpByDate)
      dfGlobalReport.coalesce(1).write.mode(SaveMode.Overwrite).json(pathReport)

      dfGlobalReport.show()

      true
    }
    catch {
      case e: (Exception)  => {
        log.error(s"error " + this.getClass + "def createReport "+e.getStackTrace)
        throw e
      }
    }
  }

  //gzPath is the file access.log.gz
  def createReportSeparatorLogs(gzPath: String, outputPath: String, nbMax:Int)(implicit spark: SparkSession):Unit={
    try {
      val df = com.project3.impl.Report.convertLogToDataset(gzPath)

      //all the dates having too big number of connection (> nbMax)
      import org.apache.spark.sql.functions._
      val dfnbConBig = df.groupBy("ip").count()
                        .filter(col("count") >= nbMax)
                        .coalesce(1).write.mode(SaveMode.Overwrite).json(outputPath+"Report_NbConBig")

      //for each date, compute the list of number of access by URI for each URI
      val dfnbUriByDate = df.groupBy("date", "url").count()
                            .coalesce(1).write.mode(SaveMode.Overwrite).json(outputPath+"Report_NbUriByDate")

      //for each date, compute the list of number of access per IP address for each IP address
      val dfnbIpByDate = df.groupBy("date", "ip").count()
                            .coalesce(1).write.mode(SaveMode.Overwrite).json(outputPath+"Report_NbIpByDate")
    }
    catch {
      case e: (Exception)  => {
        log.error(s"error " + this.getClass + "def createReport "+e.getStackTrace)
        throw e
      }
    }

    Unit
  }


  def convertLogToDataset(csvData: String)(implicit spark: SparkSession): Dataset[Access] = {
    import spark.implicits._
    try {
      //create Dataset[Access] with pre DataFrame columns order forces
      spark.read.format("csv")
        .option("header", "true")
        .option("inferSchema", "true")
        .load(csvData)
        .toDF("ip", "date", "time", "zone", "cik", "accession", "url", "code", "size", "idx", "norefer", "noagent", "find", "crawler", "browser")
        .as[Access]
    }
    catch {
      case e: (Exception)  => {
        log.error(s"error " + this.getClass + "def convertLogToDataset"+e.getStackTrace)
        throw e
      }
    }
  }
}

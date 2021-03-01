package com.project3

import com.utils.SparkJob


object MainProject3  extends App {
  @transient lazy val log = org.apache.log4j.LogManager.getLogger(MainProject3.getClass)

  log.info(s"start MainProject3 job process")

  var datapath = "src/main/resources/accesslogsdsti/" //default dsti module project logs
  //var datapath = "src/main/resources/accesslogs/" //option logs with separators
  var outputReportPath = "reports/"
  println("datapath logs default value = " + datapath)
  println("outputReportPath default value = " + outputReportPath)

  if (args.length < 2) {
    println("need 2 parameters in command line / we will take default values datapath logs and outputReportPath")
  }
  else {
    datapath = args(0)         //"arg 0 for example src/main/resources/accesslogsdsti/"
    outputReportPath = args(1) //"arg 1 for example reports/"
  }

    //get sparkSession
    implicit val spark = SparkJob.spark

    //launch process on data
    try {
      com.project3.impl.Report.createReportDstiLogs(datapath, outputReportPath, 10) //dsti module project logs
      //com.project3.impl.Report.createReportSeparatorLogs(datapath, outputReportPath, 10) //option logs with separators
    }
    catch {
      case e: (Exception)  => {
        log.error(s"error " + this.getClass + "def createReport "+e.getStackTrace)
        throw e
      }
    }
  //} end

  print("end job process")
  log.info(s"end MainProject3 job process")
}

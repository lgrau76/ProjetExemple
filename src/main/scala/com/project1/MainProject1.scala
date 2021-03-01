package com.project1

import com.utils.SparkJob


object MainProject1 extends App {

  print("start job process")

  //get sparkSession
  implicit val spark = SparkJob.spark

  //set data path file
  val datapath = "src/main/resources/fileProject1.csv"

  //launch process on data
  val ds = com.project1.impl.ExtractProcess.convertToDataset(datapath)

  print("end job process")
}

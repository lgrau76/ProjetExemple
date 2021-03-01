package com.project2.impl

import org.apache.spark.sql.SparkSession

case class MorseCode(
                      sigle: String,
                      code: String
                    )

object MorseProcess {

  def morseEncode(encodingForChar: scala.collection.Map[String, String], msg:Array[String])(implicit spark: SparkSession):String = {
    var out = ""
    def getEncode(s:String):String={
      out = out.concat(encodingForChar.get(s).mkString)
      out
    }
    for (s <- msg) yield getEncode(s)
    print("encode " + msg.mkString + " => " + out + "\n")
    out
  }



  def morseDecode(decodingForChar: scala.collection.Map[String, String], msg:Array[String])(implicit spark: SparkSession):String = {
      var out = ""
    def getEncode(s:String):String={
      out = out.concat(decodingForChar.get(s).mkString)
      out
    }
    for (s <- msg) yield getEncode(s)
    print("decode " + msg.mkString  + " => " + out + "\n")
    out
  }

  def initMorseCodeDataset(morsepath: String)(implicit spark: SparkSession): (scala.collection.Map[String, String],scala.collection.Map[String, String]) = {
      import spark.implicits._

      //Dataset[MorseCode]
      val df =  spark.read.format("csv")
        .option("header", "true")
        .option("inferSchema", "true")
        .load(morsepath)
        .toDF("sigle","code")
        .as[MorseCode]

      val encodingForChar: scala.collection.Map[String, String] = df.rdd.map(row => row.sigle -> row.code).collectAsMap()
      val decodingForChar: scala.collection.Map[String, String] = df.rdd.map(row => row.code  -> row.sigle).collectAsMap()
      (encodingForChar,decodingForChar)
    }


}

package com.project2

import com.utils.SparkJob
import com.project2.impl.MorseProcess

object MainProject2 extends App {

  print("start job process")

  //get sparkSession
  implicit val spark = SparkJob.spark

  //set morse path file
  val morsepath = "src/main/resources/fileProject2_morse_code.csv"
  val coders = MorseProcess.initMorseCodeDataset(morsepath)

  //launch Encode Msg process
  val msgToEncode = "s o s".toUpperCase.split(" ")
  val encodedMsg = MorseProcess.morseEncode(coders._1, msgToEncode)

  //launch Decode Msg process
  val msgToDecode = "----- -.-".split(" ")  //O K
  val decodedMsg = MorseProcess.morseDecode(coders._2, msgToDecode)

  print("msgToEncode => " + msgToEncode + " => "  + "encodedMsg => " + encodedMsg + "\n")
  print("msgToDecode => " + msgToDecode + " => "  + "decodedMsg => " + decodedMsg + "\n")

  print("end job process")

}

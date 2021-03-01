package project2

import com.utils.SparkSessionTestWrapper
import org.scalatest.{FunSuite, GivenWhenThen, Inspectors, Matchers}

import com.project2.impl.MorseProcess

class TestHatProject2 extends FunSuite with Matchers with Inspectors with GivenWhenThen
  with SparkSessionTestWrapper{
  lazy val spark = _spark

  /*
    //set morse path file
  val morsepath = "src/main/resources/fileProject2_morse_code.csv"
  val coders = MorseProcess.initMorseCodeDataset(morsepath)

  //launch Encode Msg process
  val msgToEncode = "s o s".toUpperCase.split(" ")
  val encodedMsg = MorseProcess.morseEncode(coders._1, msgToEncode)

  //launch Decode Msg process
  val msgToDecode = "----- -.-".split(" ")  //O K
  val decodedMsg = MorseProcess.morseDecode(coders._2, msgToDecode)
   */

  test("launch Project2 encode.........)"){
    Given("Given data src/main/resources/fileProject1.csv.........)")
    //set data path file
    val morsepath = "src/main/resources/fileProject2_morse_code.csv"
    //set morse code path file
    val coders = MorseProcess.initMorseCodeDataset(morsepath)

    When("When msgToEncode s o s and encodedMsg")
    //launch process on data
    val msgToEncode = "s o s".toUpperCase.split(" ")
    val encodedMsg = MorseProcess.morseEncode(coders._1, msgToEncode)
    //val decodedMsg = MorseProcess.morseDecode(coders._2, msgToDecode)

    Then("Then encodedMsg shouldBe " + encodedMsg)
    encodedMsg.shouldBe("...---...")

  }

  test("launch Project2 decode.........)"){
    Given("Given data src/main/resources/fileProject1.csv.........)")
    //set data path file
    val morsepath = "src/main/resources/fileProject2_morse_code.csv"
    //set morse code path file
    val coders = MorseProcess.initMorseCodeDataset(morsepath)
    val msgToDecode = "----- -.-".split(" ")  //O K

    When("When msgToEncode s o s and encodedMsg")
    //launch process on data
    val decodedMsg = MorseProcess.morseDecode(coders._2, msgToDecode)
    val targetMsg : String  = "OK"

    Then("Then decodedMsg shouldBe " + "OK" + "  decodedMsg =>" + "OK")
    decodedMsg shouldBe(targetMsg)
  }

}
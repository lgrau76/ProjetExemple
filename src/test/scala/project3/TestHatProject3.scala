package project3

import com.utils.SparkSessionTestWrapper
import org.scalatest.{FunSuite, GivenWhenThen, Inspectors, Matchers}

class TestHatProject3 extends FunSuite with Matchers with Inspectors with GivenWhenThen
  with SparkSessionTestWrapper{
  lazy val spark = _spark

  test("launch Project3 create reports.........)"){
    Given("Given data logs datapath = arg 0 for example src/main/resources/accesslogsdsti/ and outputReportPath = arg 1 for example reports/.........)")
    //set data path file
    val datapath = "src/main/resources/accesslogsdsti/" //dsti module project logs
    val outputReportPath = "reports/"

    When("When msgToEncode s o s and encodedMsg")
    //launch process on data
    val status = com.project3.impl.Report.createReportDstiLogs(datapath, outputReportPath, 10)

    Then("Then reports process shouldBe true if finish normally")
    status shouldBe(true)

  }

}

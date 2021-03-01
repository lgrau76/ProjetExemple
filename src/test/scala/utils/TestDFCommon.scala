package utils

import com.utils.{DFCommon, SparkSessionTestWrapper}
import org.scalatest.{FunSuite, GivenWhenThen, Inspectors, Matchers}

class TestDFCommon extends FunSuite with Matchers with Inspectors with GivenWhenThen with DFCommon
  with SparkSessionTestWrapper{
  lazy val spark = _spark

  test("Launch com.utils.DFCommon def readDataFromFileSrc(file_type: String, file_location: String)(implicit spark: SparkSession): DataFrame........."){
    Given("given data src/main/resources/fileProject1.csv.........)")
    //set data path file
    val file_location = "src/main/resources/fileProject1.csv"
    val file_type = "csv"

    When("When readDataFromFileSrc(file_type: String, file_location: String)")
    //launch process on data
    val ds = readDataFromFileSrc(file_type, file_location)

    Then("Thend ds.count()) shouldEqual(3).........ds.count() equals" +  ds.count())
    ds.count() shouldEqual(4)
  }

}

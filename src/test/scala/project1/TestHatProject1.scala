package project1

import com.utils.SparkSessionTestWrapper
import org.scalatest.{FunSuite, GivenWhenThen, Inspectors, Matchers}

class TestHatProject1 extends FunSuite with Matchers with Inspectors with GivenWhenThen
  with SparkSessionTestWrapper{
  lazy val spark = _spark

  test("launch Project1 General.........)"){
    Given("Given data src/main/resources/fileProject1.csv.........)")
    //set data path file
    val datapath = "src/main/resources/fileProject1.csv"

    When("When ExtractProcess.convertToDataset(datapath).........)")
    //launch process on data
    val ds = com.project1.impl.ExtractProcess.convertToDataset(datapath)

    Then("Thend ds.count() shouldEqual(3).........ds.count() equals" +  ds.count())
    ds.count() shouldEqual(3)
  }
}
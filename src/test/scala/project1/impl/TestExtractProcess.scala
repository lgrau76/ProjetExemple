package project1.impl

import com.utils.SparkSessionTestWrapper
import org.scalatest.{FunSuite, GivenWhenThen, Inspectors, Matchers}

class TestExtractProcess extends FunSuite with Matchers with Inspectors with GivenWhenThen
  with SparkSessionTestWrapper{
  lazy val spark = _spark

  test("Launch com.utils.DFCommon def readDataFromFileSrc(file_type: String, file_location: String)(implicit spark: SparkSession): DataFrame........."){
    Given("given data src/main/resources/fileProject1.csv.........)")
    //set data path file
    val csvData = "src/main/resources/fileProject1.csv"
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

    When("When readDataFromFileSrc(file_type: String, file_location: String)")
    //launch process on data
    val ds = com.project1.impl.ExtractProcess.convertToDataset(csvData)

    Then("Then ds equals 3")
    ds.count() shouldBe 3 //ds Dataset[PrecipitationSample] class
  }

}


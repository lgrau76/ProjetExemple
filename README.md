## Installation and configuration environment
### Installations base
- install Intellij Community Edition
- copy winutils.exe in c:\winutils\bin\
- set HADOOP_HOME variable environment

### Create GitHub Repository DstiGrauLaurentProjectDataPipeline2sbtversionsbtversion
- Create Local Repository windows : DstiGrauLaurentProjectDataPipeline2sbtversionsbtversion
- cd DstiGrauLaurentProjectDataPipeline2sbtversionsbtversion
- git init
- create .gitignore file *.log *.zip
- create README.md file
- git add .
- git branch -M main
- git remote add origin https://github.com/lgrau76/DstiGrauLaurentProjectDataPipeline2sbtversion.git
- git checkout -b development
- git status to verify exclude files
- git add .
- git commit -m "commit project"
- git push origin HEAD

## Projects to do
### Project1 provide implementation of the function "def convertToDataset(csvData: String): Dataset[PrecipitationSample]"
- intellij development / maven / spark / scala
- package com.project1
- main com.project1.MainProject1
- data stored in src/main/resources/fileProject1.csv
- git add / commit / push data to dev branch
- main arg 1 => src/main/resources/fileProject1.csv
- debug/test/build in intellij

#### Run Test Project1
- test/scala/project1/TestHatProject1

#### Test Run Package
- sbt compile
- sbt test
- sbt run 1
- sbt package
- Run application locally on all cores :
  ./bin/spark-submit \
  --class com.project1.MainProject1 \
  --master local[*] \
  dstigraulaurentprojectdatapipeline2sbtversion_2.11-0.1.jar \
  src/main/resources/fileProject1.csv


### Project2 Write a morseDecode function "def morseDecode(s: String): String" able to decode any morseEncoded string with all morse supported chars (A-Z and 0-9)
#### Solution
- package com.project2
- morse code stored in src/main/resources/fileProject2_morse_code.csv
- git add / commit / push data to dev branch
- main => com.project1.MainProject1
- main arg 1 => src/main/resources/fileProject2_morse_code.csv
- main arg 2 => sos
  console output result encode => ...---...  
- main arg 3 => ----- -.-
  console output result decode => ok

#### Run Test Project2
- test/scala/project2/TestHatProject2

#### Control result report files in directories (from root project) :
- report/Report_NbConBig
- report/Report_NbIpByDate
- report/Report_NbUriByDate

#### Run Package   
- sbt compile
- sbt test
- sbt run 2
- sbt package
- Run application locally on all cores :
            ./bin/spark-submit \
                --class com.project2.MainProject2 \
                --master local[*] \
                dstigraulaurentprojectdatapipeline2sbtversion_2.11-0.1.jar \
                src/main/resources/fileProject2_morse_code.csv \
                sos \
                ----- -.- 

### Project3 Create Reports from logs
object Report is logic class with :
- the function find all the dates having too big number of connection (> 20000)
- agreagate simple report json in 1 file in dir report/Report_Global

#### Solution
- package => com.project3
- git add / commit / push data to dev branch
- Create a scala file containing a function => MainProject3
- Create a scala file containing a function => com.project3.impl.Report.scala
    . gzPath is the directory src/main/resources/accesslogsdsti/
    . the function find all the dates having too big number of connection (> 20000)
    . for each date:
        compute the list of number of access by URI for each URI
        compute the list of number of access per IP address for each IP address
        generate at outputPath a report in json format with one json report line per date
    . case class with the right attributes names and types
    . save the DataFrame json 
      function (see https://sparkbyexamples.com/spark/spark-read-and-write-json-file/ for more information)
  
- main 2 args =>
     datapath = "src/main/resources/accesslogsdsti/"
          contains access.log (in .gitignore not in git because too heavy to push or get)
     outputReportPath = "reports/"
    
- coalesce(1) write DataFrame report to reduce partition file to only 1 (all data report in file)
- log4j logs => trace info + warn + error
- try / catch management

#### Run Test Project3 Create Reports from logs
- test/scala/project3/TestHatProject3

#### Run Package
- sbt compile
- sbt test
- sbt run 3
- sbt package
- sbt assembly
- Run application locally on all cores :
  ./bin/spark-submit \
  --class com.project2.MainProject2 \
  --master local[*] \
  dstigraulaurentprojectdatapipeline2sbtversion_2.11-0.1.jar src/main/resources/accesslogsdsti/ reports/
  

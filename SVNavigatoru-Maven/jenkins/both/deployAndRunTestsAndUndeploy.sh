#!/bin/bash

if [ $# != 1 ]; then
	echo "

  -----------------------------------------------------------------------
  Usage of deployAndRunTestsAndUndeploy.sh
  -----------------------------------------------------------------------

  ./deployAndRunTestsAndUndeploy.sh <test_category>

  <test_category> ... Name of the class in the package svnavigatoru.test.category
                      which represents a category of tests which you want to evaluate.
                      The name is without a prefix of package.
                      Example: FastTests

  Sample usage:
  ./deployAndRunTestsAndUndeploy.sh FastTests

"
	exit 1
fi

# Goes to the home of the project.
cd d:
cd Development/Java/SourceFiles/Spring/SVNavigatoru-Maven/


# Cleans up before a compilation.
#mvn clean
mvn tomcat:undeploy


# Compiles, packages and deploys to a Tomcat. No tests are evaluated.
mvn tomcat:deploy -DskipTests


# Runs tests which belong to a category specified in the first argument.
mvn test -DargLine="-Dtests.selenium.port=8081" -Dtests.category="svnavigatoru.test.category.$1"


# Cleans up at the end.
#mvn clean
mvn tomcat:undeploy

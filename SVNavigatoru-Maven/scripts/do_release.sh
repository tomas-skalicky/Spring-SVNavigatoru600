# Tests whether all tests are satisfied (but Selenium ones).
mvn clean compile
mvn test -Dtests.category=com.svnavigatoru600.test.category.ControllerIntegrationTests
mvn test -Dtests.category=com.svnavigatoru600.test.category.JsoupTests
mvn test -Dtests.category=com.svnavigatoru600.test.category.PersistenceTests
mvn test -Dtests.category=com.svnavigatoru600.test.category.UnitTests

mvn -DskipTests release:prepare -Darguments="-DskipTests"
mvn -DskipTests release:perform -Darguments="-DskipTests"


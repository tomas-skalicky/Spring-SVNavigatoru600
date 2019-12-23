1) Start the Sonatype Nexus with
   - cd /home/tom/Documents/Dropbox/private/scripts/docker/sonatype_nexus; ./start_nexus.bash

2) Start the Jenkins with
   - cd /home/tom/Documents/Dropbox/private/scripts/docker/jenkins; ./start_jenkins_master.bash
   - Go to Jenkins on http://localhost:8080/ (credentials are in KeePass)

3) Execute tests on the command line (all should be successful):
   - cd /home/tom/Documents/development/Java/SourceFiles/Spring/SVNavigatoru/SVNavigatoru-Maven
   - mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.UnitTests
   - mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.PersistenceTests
   - mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.IntegrationTests

4) Trigger a Jenkins job http://localhost:8080/job/svnavigatoru--unit-and-integration-tests/ which should be successful.

5) Start the Tomcat locally by
   - cd /home/tom/Documents/development/Java/SourceFiles/Spring/SVNavigatoru/SVNavigatoru-Maven; ./tomcat-debug.sh
   - Prove it works by going on http://localhost:9080/svnavigatoru600

6) Database
   - Either use mysql-workbench command to start a database client or
   - Use
     cd /home/tom/Documents/development/Java/SourceFiles/Spring/SVNavigatoru/SVNavigatoru-Maven/scripts; ./connect_to_test_database.bash

7) Execute tail -F /home/tom/Documents/development/Java/SourceFiles/Spring/SVNavigatoru/SVNavigatoru-Maven/target/tomcat/logs/svnavigatoru600.log
   to observe a log file.

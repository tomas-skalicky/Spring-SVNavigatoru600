#!/usr/bin/env bash

set -e

# JDK 8 starts a javadoc check which fails -> we need JDK 6 or JDK 7
JAVA_HOME_BACKUP=$JAVA_HOME

if [[ -z $JDK7_HOME ]]; then
    JDK7_HOME=/usr/lib/jvm/jdk7
fi
JAVA_HOME=$JDK7_HOME
echo "INFO: Current JAVA_HOME is $JAVA_HOME"


# Tests whether all unit and integration tests are satisfied (except availability and Selenium tests)
# If so, it performs release.
(cd ../ \
    && mvn clean compile \
    && mvn test -Dtests.category=com.svnavigatoru600.test.category.ControllerIntegrationTests \
    && mvn test -Dtests.category=com.svnavigatoru600.test.category.PersistenceTests \
    && mvn test -Dtests.category=com.svnavigatoru600.test.category.UnitTests \
    && mvn -DskipTests release:prepare -Darguments=-DskipTests \
    && mvn -DskipTests release:perform -Darguments=-DskipTests \
    && echo -e '\nINFO: release has been SUCCESSFUL\n')


# Sets the JAVA_HOME environment variable back.
JAVA_HOME=$JAVA_HOME_BACKUP


#!/usr/bin/env bash

set -e

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


#!/usr/bin/env bash

#######
# This script packs the web app to the WAR file which is ready to be deployed
# to the svnavigatoru600.com
#
# author: Tomas Skalicky
#######

set -e

required_java_version=1.8.0

assert_java_version() {
    local -r java_command=${1:?}
    local -r first_line_of_java_version="$(head --lines=1 <($java_command -version 2>&1))"
    # Bash replacement construct
    local -r java_version_with_quotes="${first_line_of_java_version/java version /}"
    if [[ ! $java_version_with_quotes =~ ^\"${required_java_version}_.*\"$ ]]; then
        print_error "Required Java version is $required_java_version"
    fi
}

assert_java_version "$JAVA_HOME/bin/java"
assert_java_version "$(which java)"

initial_path=$(pwd)
cd -P ..
project_home=$(pwd)

mvn clean package -DskipTests
mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.UnitTests
mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.PersistenceTests
mvn verify -P coverage,run-integration-tests --debug -Dtests.category=com.svnavigatoru600.test.category.ControllerIntegrationTests

build_home=$project_home/target
cd "$build_home"


# Renames the package to the target name.
target_package_name=ROOT.war
mv --verbose svnavigatoru600*.war "$target_package_name"


# Unpacks the output package of the maven.
tweak_dir=ROOT.war_tweak
mkdir "$tweak_dir"
cd "$tweak_dir"
jar xf ../"$target_package_name"

# Sets up the production JDBC configuration instead of the development one.
path_to_jdbc_config=WEB-INF/classes
mv --verbose "$path_to_jdbc_config"/jdbc-production.properties "$path_to_jdbc_config"/jdbc.properties

# Packs all files to create the desired output WAR file.
jar cf "$target_package_name" ./*
mv --verbose "$target_package_name" "$build_home"/

cd "$initial_path"


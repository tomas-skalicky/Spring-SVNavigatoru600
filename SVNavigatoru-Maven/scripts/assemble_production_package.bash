#!/usr/bin/env bash

#######
# This script packs the web app to the WAR file which is ready to be deployed
# to the svnavigatoru600.com
#
# author: Tomas Skalicky
#######

set -e

# JDK 8 starts a javadoc check which fails -> we need JDK 6 or JDK 7
JAVA_HOME_BACKUP=$JAVA_HOME

if [[ -z $JDK7_HOME ]]; then
    JDK7_HOME=/usr/lib/jvm/jdk7
fi
JAVA_HOME=$JDK7_HOME
echo "INFO: Current JAVA_HOME is $JAVA_HOME"



PROJECT_HOME=..
cd -P "$PROJECT_HOME"
PROJECT_HOME=$(pwd)

mvn clean package -DskipTests

BUILD_HOME=$PROJECT_HOME/target
cd "$BUILD_HOME"


# Renames the package to the target name.
MAVEN_PACKAGE_NAME=svnavigatoru600*.war
TARGET_PACKAGE_NAME=ROOT.war
mv "$MAVEN_PACKAGE_NAME" "$TARGET_PACKAGE_NAME"


# Unpacks the output package of the maven.
TWEAK_DIR=ROOT.war_tweak
mkdir "$TWEAK_DIR"
cd "$TWEAK_DIR"
jar xf ../"$TARGET_PACKAGE_NAME"

# Sets up the production JDBC configuration instead of the development one.
PATH_TO_JDBC_CONFIG=WEB-INF/classes
ACTIVE_JDBC_CONFIG=$PATH_TO_JDBC_CONFIG/jdbc.properties
PRODUCTION_JDBC_CONFIG=$PATH_TO_JDBC_CONFIG/jdbc-production.properties
mv "$PRODUCTION_JDBC_CONFIG" "$ACTIVE_JDBC_CONFIG"

# Packs all files to create the desired output WAR file.
jar cf "$TARGET_PACKAGE_NAME" *
mv "$TARGET_PACKAGE_NAME" "$BUILD_HOME"/


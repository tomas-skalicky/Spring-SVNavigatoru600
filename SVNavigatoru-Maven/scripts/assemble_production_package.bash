#!/usr/bin/env bash

#######
# This script packs the web app to the WAR file which is ready to be deployed
# to the svnavigatoru600.com
#
# author: Tomas Skalicky
#######

set -e

initial_path=$(pwd)
cd -P ..
project_home=$(pwd)

mvn clean package -DskipTests

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


#!/usr/bin/env bash

set -e

check_webapp_is_running() {
    local -r http_code=$(curl \
                              --silent \
                              --location \
                              --write-out '%{http_code}' \
                              --output /dev/null \
                              http://localhost:8080/svnavigatoru600)
    if (( $http_code != 200 )); then
        # Not error since the web app can be started after logstash-forwarder.
        printf '%s' 'WARNING: SVNavigatoru600 webapp is not running locally.' \
                    ' Use ./tomcat_start.sh script.'
        printf '\n'
    fi
}

check_webapp_is_running

service logstash-forwarder start


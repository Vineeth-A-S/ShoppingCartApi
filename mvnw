#!/bin/sh
MAVEN_BIN="/home/vineeth/.m2/wrapper/dists/apache-maven-3.10.0-bin/39q0qgmngdu329psje7bjgne79/apache-maven-3.10.0/bin/mvn"
if [ -x "$MAVEN_BIN" ]; then
    exec "$MAVEN_BIN" "$@"
fi
exec mvn "$@"

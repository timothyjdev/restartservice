#!/bin/bash

# This script runs a Jar file that searches a log file for a specific term.
# If the term is found the Jar file creates a file called NeedToRestart.txt.
# This script will stop Tomcat then delete the Tomcat log file as well as
# NeedToRestart.txt. Tomcat is then started. This script is to restart Tomcat
# when there is a PermGen error and it is meant to be run in a development
# environment only.

$JAVA_HOME -jar RestartService-0.0.1.jar $1 $2


if [ -e "NeedToRestart.txt" ]; then
   service tomcat6 stop
   rm -f "$1"
   rm -f "NeedToRestart.txt"
   service tomcat6 start
fi

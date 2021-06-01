#!/bin/bash

DERBY_PATH="/opt/Apache/db-derby-10.14.2.0-bin"
DERBY_JAR="$DERBY_PATH/lib/derbyrun.jar"

javac -cp .:lib/* AddToDerby.java

java -cp .:lib/* AddToDerby

printf "\nYou'll be redirected to ij terminal. Use it to connect with database created.\n\n"
printf "Database Created would be 'assignOne' and table created would be 'Pedestrian'\n\n\n"

printf "Use Following command as it is after ':'  to connect to derby  : CONNECT 'jdbc:derby:assignOne;create=true'; \n\n"
printf "Make Sure table 'Pedestrian' is created by doing : SHOW TABLES; \n"

java -jar $DERBY_JAR ij


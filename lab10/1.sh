#!/bin/bash

PROJECT_DIR=~/java/lab10
LIB_DIR=$PROJECT_DIR/lib
JAR_NAME=mariadb-java-client-3.5.7.jar
JAVA_FILE=LibraryAppFull.java
CLASS_NAME=LibraryAppFull
DB_NAME=library_db
DB_USER=root
DB_PASS=root
DB_HOST=localhost
DB_PORT=3306

if [ ! -f "$LIB_DIR/$JAR_NAME" ]; then
    echo "Error: MariaDB JDBC driver $JAR_NAME not found in $LIB_DIR"
    echo "Please download it from https://mariadb.com/downloads/connectors/java"
    exit 1
fi

echo "Checking database connection..."
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS -e "USE $DB_NAME;" &> /dev/null
if [ $? -ne 0 ]; then
    echo "Error: Cannot connect to database $DB_NAME with user $DB_USER"
    exit 1
fi

echo "Compiling $JAVA_FILE..."
javac -cp .:$LIB_DIR/$JAR_NAME $PROJECT_DIR/$JAVA_FILE
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Running $CLASS_NAME..."
java -cp .:$LIB_DIR/$JAR_NAME $CLASS_NAME
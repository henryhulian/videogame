#!/bin/bash
APP="UserCenter-0.0.1-SNAPSHOT.jar"
SERVERPID=`ps -ef | grep  $APP | grep -v grep |  awk '{print $2}'`
echo kill $SERVERPID ......
kill $SERVERPID
while ps -p $SERVERPID; 
do 
sleep 1;
echo kill $SERVERPID ......; 
done


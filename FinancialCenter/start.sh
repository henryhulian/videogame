#!/bin/bash
IP=$(hostname -i)
APP="UserCenter-0.0.1-SNAPSHOT.jar"
JVM=" -server -d64 -Xms128m -Xmx2048m -XX:MaxPermSize=218M "
JMX=" -Djava.rmi.server.hostname=$IP -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=2010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false "
LOG=" -Dlogback.configurationFile=config/logback.xml "
$JAVA_HOME/bin/java $JVM $JMX $LOG -Djava.net.preferIPv4Stack=true -jar $APP &
disown
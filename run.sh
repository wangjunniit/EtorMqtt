#! /bin/bash

javahome="/opt/jdk-15"


exec $javahome/bin/java \
--add-opens=java.base/sun.net.dns=ALL-UNNAMED \
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens=java.base/java.nio=ALL-UNNAMED \
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
-Dio.netty.tryReflectionSetAccessible=true \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseZGC \
-Xlog:safepoint,classhisto*=trace,age*,gc*=info:file=Logs/gc-%t.log:time,tid,tags:filecount=5,filesize=50m \
-XX:ReservedCodeCacheSize=128m -XX:InitialCodeCacheSize=128m \
-Xms512M \
-Xmx2G \
-jar $(dirname "$0")/etormqtt.jar
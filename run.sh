#!/bin/bash
exec java \
--add-opens=java.base/sun.net.dns=ALL-UNNAMED \
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens=java.base/java.nio=ALL-UNNAMED \
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
-Dio.netty.tryReflectionSetAccessible=true \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseZGC \
-Xms512M \
-Xmx2G \
-jar $(dirname "$0")/etormqtt.jar
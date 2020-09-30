package etor;

import etor.config.BrokerConfig;
import etor.util.LogHelper;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        printRuntimeInfo();

        BrokerConfig.init();

        BrokerConfig brokerConfig = BrokerConfig.getInstance();

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(brokerConfig.getEventLoopPoolSize());
        vertxOptions.setWorkerPoolSize(brokerConfig.getWorkerPoolSize());
        vertxOptions.setPreferNativeTransport(true);

        Vertx vertx = Vertx.vertx(vertxOptions);

        boolean usingNative = vertx.isNativeTransportEnabled();
        LogHelper.getLogger().info("vertx配置：{}", vertxOptions.toString());
        LogHelper.getLogger().info("使用epoll: " + usingNative);

        MqttServer.run(vertx);
    }

    static void printRuntimeInfo() {

        LogHelper.getLogger().info("CPU核心数：{}", CpuCoreSensor.availableProcessors());

        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();

        LogHelper.getLogger().info("GC模式：");
        for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
            LogHelper.getLogger().info(gcMxBean.getName() + "\t" + gcMxBean.getObjectName());
        }
    }
}

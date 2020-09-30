package etor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import etor.config.BrokerConfig;
import etor.util.LogHelper;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.core.net.NetServer;

import java.io.File;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        int cpuCores = CpuCoreSensor.availableProcessors();
        LogHelper.getLogger().info("CPU核心数：{}", cpuCores);

        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();

        LogHelper.getLogger().info("GC模式：");
        for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
            LogHelper.getLogger().info(gcMxBean.getName() + "\t" + gcMxBean.getObjectName());
        }

        BrokerConfig.Init();
        startupVertx();
    }

    private static void startupVertx() {

        BrokerConfig brokerConfig = BrokerConfig.getInstance();

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(brokerConfig.getEventLoopPoolSize());
        vertxOptions.setWorkerPoolSize(brokerConfig.getWorkerPoolSize());
        vertxOptions.setPreferNativeTransport(true);

        Vertx vertx = Vertx.vertx(vertxOptions);
        boolean usingNative = vertx.isNativeTransportEnabled();
        LogHelper.getLogger().info("vertx配置：{}", vertxOptions.toString());
        LogHelper.getLogger().info("使用epoll: " + usingNative);

        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(CpuCoreSensor.availableProcessors());

        NetServer server = vertx.createNetServer();
        server.connectHandler(socket ->
                socket.handler(buffer ->
                        System.out.println("I received some bytes: " + buffer.length())));


        server.listen(1234, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.out.println("Failed to bind!");
            }
        });
    }

}

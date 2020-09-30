package etor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import etor.Main;
import etor.util.LogHelper;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
public class BrokerConfig {

    /**
     * mqtt服务端tcp端口号
     */
    private int mqttport = 1883;

    /**
     * 事件循环线程数
     */
    private int eventLoopPoolSize = CpuCoreSensor.availableProcessors() * 2;

    /**
     * 工作线程数
     */
    private int workerPoolSize = CpuCoreSensor.availableProcessors() * 4;

    private static BrokerConfig instance = new BrokerConfig();

    public static BrokerConfig getInstance() {
        return instance;
    }

    public static void Init() throws IOException {
        var rootDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath();

        LogHelper.getLogger().info("程序运行根目录：" + rootDir);

        var brokerConfigFilePath = Paths.get(rootDir, "broker.yaml");

        if (new File(brokerConfigFilePath.toString()).exists()) {
            var configText = new String(Files.readAllBytes(brokerConfigFilePath));

            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            instance = om.readValue(configText, BrokerConfig.class);
        } else {
            LogHelper.getLogger().warn("未发现配置文件，将使用默认配置");
        }

        LogHelper.getLogger().info("broker配置：\n{}", BrokerConfig.getInstance().toString());
    }
}

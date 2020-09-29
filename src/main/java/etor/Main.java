package etor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import etor.config.BrokerConfig;
import etor.util.LogHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        init();
    }

    private static void init() throws IOException {

        var rootDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath();

        LogHelper.getLogger().info("程序运行根目录：" + rootDir);

        var brokerConfigFilePath = Paths.get(rootDir, "broker.yaml");

        var configText = new String(Files.readAllBytes(brokerConfigFilePath));

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        BrokerConfig brokerConfig = om.readValue(configText, BrokerConfig.class);

        BrokerConfig.Init(brokerConfig);

        LogHelper.getLogger().info("配置：\n{}", BrokerConfig.getInstance().toString());
    }
}

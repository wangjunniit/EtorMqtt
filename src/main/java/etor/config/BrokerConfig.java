package etor.config;

import lombok.Data;

@Data
public class BrokerConfig {

    /**
     * mqtt服务端tcp端口号
     */
    private int mqttport;

    private static BrokerConfig instance = new BrokerConfig();

    public static BrokerConfig getInstance() {
        return instance;
    }

    public static void Init(BrokerConfig config) {
        instance = config;
    }
}

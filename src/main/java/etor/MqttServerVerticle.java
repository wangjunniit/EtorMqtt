package etor;

import etor.config.BrokerConfig;
import etor.util.LogHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttServer;

public class MqttServerVerticle extends AbstractVerticle {

    MqttServer mqttServer;

    @Override
    public void start() {

        BrokerConfig config = BrokerConfig.getInstance();
        mqttServer = MqttServer.create(vertx);

        mqttServer.endpointHandler(endpoint -> {

        }).listen(config.getMqttport(), config.getBindIp(), ar -> {
            if (ar.succeeded()) {
                LogHelper.getLogger().info("MQTT server 启动成功，端口号：" + ar.result().actualPort());
            } else {
                LogHelper.getLogger().info("MQTT server启动失败");
                ar.cause().printStackTrace();
            }
        });
    }


    @Override
    public void stop() {
        if (mqttServer != null) {
            mqttServer.close().result();
        }
    }
}

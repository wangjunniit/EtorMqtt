package etor;

import etor.config.BrokerConfig;
import etor.util.LogHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;

public class MqttServerVerticle extends AbstractVerticle {

    NetServer server;
    boolean listened = false;

    @Override
    public void start() {

        BrokerConfig config = BrokerConfig.getInstance();

        NetServer server = vertx.createNetServer();
        server.connectHandler(socket ->
                socket.handler(buffer ->
                        System.out.println("I received some bytes: " + buffer.length())));


        server.listen(config.getMqttport(), config.getBindIp(), res -> {
            if (res.succeeded()) {
                listened = true;
                LogHelper.getLogger().info("MqttServer已启动，端口：{}", config.getMqttport());
            } else {
                LogHelper.getLogger().error("MqttServer启动失败", res.cause());
            }
        });
    }

    @Override
    public void stop() {
        if (server != null && listened) {
            server.close().result();
        }
    }
}

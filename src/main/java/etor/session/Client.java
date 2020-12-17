package etor.session;

import etor.util.LogHelper;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;

import java.util.concurrent.ConcurrentHashMap;

/**
 * mqtt客户端
 */
public class Client {

    private final MqttEndpoint endpoint;

    private final ConcurrentHashMap<String, MqttQoS> subTopics;

    Client(MqttEndpoint endpoint) {
        this.endpoint = endpoint;
        subTopics = new ConcurrentHashMap<>();
    }

    /**
     * 订阅主题
     *
     * @param topic
     */
    public void subTopic(String topic, MqttQoS qos) {
        synchronized (this) {
            if (!subTopics.containsKey(topic)) {
                subTopics.put(topic, qos);
                LogHelper.getLogger().info(endpoint.clientIdentifier() + "\t订阅了主题：" + topic + "\tqos：" + qos.value());
                SubManage.getInstance().sub(endpoint.clientIdentifier(), topic);
            }
        }
    }

    /**
     * 取消订阅
     *
     * @param topic
     */
    public void unSubTopic(String topic) {
        synchronized (this) {
            subTopics.remove(topic);
            LogHelper.getLogger().info(endpoint.clientIdentifier() + "取消了订阅主题：" + topic);
            SubManage.getInstance().unSub(endpoint.clientIdentifier(), topic);
        }
    }

    /**
     * 取消订阅所有主题
     */
    void unSubAllTopic() {
        synchronized (this) {
            subTopics.forEach((topic, value) -> {
                SubManage.getInstance().unSub(endpoint.clientIdentifier(), topic);
            });
            subTopics.clear();
        }
    }

    /**
     * 发送消息到客户端
     *
     * @param topic
     * @param payload
     */
    public void publish(String topic, Buffer payload, int pubQos) {

        if (subTopics.containsKey(topic)) {

            if (endpoint.isConnected()) {

                var qos = subTopics.get(topic);
                var future = endpoint.publish(topic, payload, qos, false, false);

                future.onComplete(res -> {

                    var logContent = "topic：" + topic + "\t订阅者ClientId：" + endpoint.clientIdentifier() + "\r\n" +
                            "订阅者qos：" + qos.value() + "\t发布者qos：" + pubQos + "\t" + "消息Id：" + future.result() +
                            "\r\npayload：" + payload + "";

                    if (res.succeeded()) {
                        LogHelper.getLogger().info("发送消息成功\t" + logContent);
                    } else {
                        LogHelper.getLogger().error("消息发送失败\t" + logContent);
                    }
                });
            }
        }

    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof Client) {

            return ((Client) object).endpoint.clientIdentifier().equals(endpoint.clientIdentifier());
        }

        return false;
    }
}

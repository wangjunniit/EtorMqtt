package etor.session;

import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttEndpoint;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端session管理
 */
public class SessionManage {
    /**
     * key:clientId
     */
    private static ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();

    private static SessionManage instance = new SessionManage();

    public static SessionManage getInstance() {
        return instance;
    }

    /**
     * 添加客户端
     *
     * @param endpoint
     */
    public void addClient(MqttEndpoint endpoint) {

        if (!clients.containsKey(endpoint.clientIdentifier())) {

            clients.put(endpoint.clientIdentifier(), new Client(endpoint));

        }
    }

    /**
     * 移除客户端
     *
     * @param clientId
     */
    public void removeClient(String clientId) {

        var client = getOneClientById(clientId);

        if (client == null) {

            return;
        }

        client.unSubAllTopic();

        clients.remove(clientId);

    }

    /**
     * 根据clientId获取客户端
     *
     * @param clientId
     * @return
     */
    public Client getOneClientById(String clientId) {

        if (clientId == null) {

            return null;
        }

        return clients.getOrDefault(clientId, null);
    }

    /**
     * 客户端数量
     *
     * @return
     */
    public int clientCount() {

        return clients.size();
    }
}

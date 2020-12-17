package etor.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 订阅管理
 */
public class SubManage {
    /**
     * key:topic value:clients
     */
    private static ConcurrentHashMap<String, CopyOnWriteArrayList<Client>> subs = new ConcurrentHashMap<>();

    private static SubManage instance = new SubManage();

    public static SubManage getInstance() {
        return instance;
    }

    /**
     * 订阅主题
     *
     * @param clientId
     * @param topic
     */
    synchronized void sub(String clientId, String topic) {

        if (!subs.containsKey(topic)) {

            subs.put(topic, new CopyOnWriteArrayList<>());
        }

        var clients = subs.get(topic);

        var client = SessionManage.getInstance().getOneClientById(clientId);

        if (client != null) {

            if (!clients.contains(client)) {

                clients.add(client);
            }
        }
    }

    /**
     * 取消订阅
     *
     * @param clientId
     * @param topic
     */
    synchronized void unSub(String clientId, String topic) {

        var clients = subs.getOrDefault(topic, null);

        var client = SessionManage.getInstance().getOneClientById(clientId);

        if (client != null) {

            if (clients != null) {

                clients.removeIf(s -> s.equals(client));

                if (clients.size() == 0) {

                    subs.remove(topic);
                }
            }
        }

    }

    /**
     * 根据主题获取订阅该主题的所有客户端
     *
     * @param topic
     * @return
     */
    public CopyOnWriteArrayList<Client> getClientsByTopic(String topic) {

        if(topic == null){

            return null;
        }

        return subs.getOrDefault(topic, null);
    }
}

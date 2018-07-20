package com.wangpos.mqtt.client;

import com.wangpos.mqtt.callback.PushCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by zx on 2017/6/28.
 */
public class ClientMQTT {

    public static final String HOST = "tcp://127.0.0.1:61613";
    public MqttClient client;
    public MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";

    /**
     * 构造函数
     * @throws MqttException
     */
    public ClientMQTT(String clientid) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        // MQTT的连接设置
        options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(false);
        // 设置连接的用户名
        options.setUserName(userName);
        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        // 设置回调
        client.setCallback(new PushCallback());
        MqttTopic topic = client.getTopic("topic");
        //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        options.setWill(topic, "你已经掉线".getBytes(), 2, true);
        try {
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            //订阅消息  订阅多个消息
            int[] Qos  = {1};
            String[] topic4 = {"enMessage/+"};
            client.subscribe(topic4, Qos);

            String[] topicFilters ={"$SYS/broker/clients/connected","$SYS/broker/publish/messages/sent","$SYS/broker/clients/total"} ;
            client.subscribe(topicFilters,new int[]{1,1,1});
            System. out .println( "Subscribe success for: "+topicFilters.toString());

//            //当前连接的客户端数目。
//            String[] topic4 = {"$SYS/broker/clients/connected"};
//            client.subscribe(topic4, Qos);
//
//            //从代理服务器开机开始发布的信息总数。
//            String[] topic5 = {"$SYS/broker/publish/messages/sent"};
//            client.subscribe(topic5, Qos);
//
//            //服务器订阅主题总数
//            String[] topic6 = {"$SYS/broker/subscriptions/count"};
//            client.subscribe(topic6, Qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public static void main(String[] args) throws MqttException {
            ClientMQTT client = new ClientMQTT("client1");
            client.start();
    }
}

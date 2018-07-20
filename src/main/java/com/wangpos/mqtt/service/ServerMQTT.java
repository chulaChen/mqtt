package com.wangpos.mqtt.service;

import com.wangpos.mqtt.callback.PushCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * Created by zx on 2017/6/28.
 */
public class ServerMQTT {

    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://127.0.0.1:61613";
    public MqttClient client;
    private String userName = "admin";
    private String passWord = "password";
    public MqttMessage message;
    public String topicName;

    /**
     * 构造函数
     * @throws MqttException
     */
    public ServerMQTT(String clientid) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布主题消息
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "+ token.isComplete());
    }

    /**
     *  启动入口
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException {

        //发布主题消息1
        ServerMQTT server = new ServerMQTT("serve1");
        server.message = new MqttMessage();
        server.message.setQos(1);
        server.message.setRetained(true);
        server.message.setPayload("绑定应用成功".getBytes());
        MqttTopic topic1 = server.client.getTopic("enMessage/en1");
        server.publish(topic1 , server.message);
        System.out.println(server.message.isRetained() + "------ratained状态");
        System.out.println("---------------------------------------------------------------------------");

        //发布主题消息2
        ServerMQTT server2 = new ServerMQTT("serve2");
        server2.message = new MqttMessage();
        server2.message.setQos(1);
        server2.message.setRetained(true);
        server2.message.setPayload("解绑应用成功".getBytes());
        server2.client.publish("enMessage/en2", server2.message);
        System.out.println(server2.message.isRetained() + "------ratained状态");
        System.out.println("---------------------------------------------------------------------------");

    }
}

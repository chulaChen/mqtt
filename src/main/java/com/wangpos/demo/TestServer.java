package com.wangpos.demo;

import com.wangpos.mqtt.service.ServerMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * Created by zx on 2017/7/4.
 */
public class TestServer {

    public static void main(String[] args) throws MqttException {
        TestServer testServer = new TestServer();

        for(int i=0;i< 10;i++){
            ServerMQTT serverMQTT = new ServerMQTT("serve1"+i);
            //Thread 1
            new Thread(testServer.new ProductorMq(serverMQTT)).start();
        }

    }

    private class ProductorMq implements Runnable{
        ServerMQTT serverMQTT;
        public ProductorMq(ServerMQTT serverMQTT){
            this.serverMQTT = serverMQTT;
        }

        public void run() {
            while(true){
                try {
                    serverMQTT.message = new MqttMessage();
                    serverMQTT.message.setQos(1);
                    serverMQTT.message.setRetained(true);
                    serverMQTT.message.setPayload("绑定应用成功".getBytes());
                    MqttTopic topic1 = serverMQTT.client.getTopic("en1");
                    serverMQTT.publish(topic1 , serverMQTT.message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.wangpos.demo;

import com.wangpos.mqtt.client.ClientMQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by zx on 2017/7/4.
 */
public class TestClient {

    public static void main(String[] args) throws MqttException {

        TestClient testConsumer = new TestClient();
        new Thread(testConsumer.new ConsumerMq("A")).start();
        new Thread(testConsumer.new ConsumerMq("B")).start();
        new Thread(testConsumer.new ConsumerMq("C")).start();
        new Thread(testConsumer.new ConsumerMq("D")).start();
        new Thread(testConsumer.new ConsumerMq("E")).start();
        new Thread(testConsumer.new ConsumerMq("F")).start();
        new Thread(testConsumer.new ConsumerMq("G")).start();
        new Thread(testConsumer.new ConsumerMq("H")).start();
        new Thread(testConsumer.new ConsumerMq("I")).start();
        new Thread(testConsumer.new ConsumerMq("J")).start();
    }

    private class ConsumerMq implements Runnable{
        private  String threadName;
        ConsumerMq(String threadName){
            this.threadName = threadName;
        }
        public void run() {
            while(true) try {
                int i = 1;
                System.out.println(threadName);
                ClientMQTT clientMQTT = new ClientMQTT("client_"+threadName + "_"+ i);
                clientMQTT.start();
                i = i+1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

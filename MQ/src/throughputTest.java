import org.junit.Test;

import java.util.Scanner;

public class throughputTest {

    public void testThroughputAll() {
        //修改Config中的type
        Config.type = 1;
        Broker t = new Broker("1");
        new Thread(t).start();
        Entity publisher = new Entity(1, 0);
        Entity subscriber = new Entity(1, 8080);
        new Thread(subscriber).start();
        subscriber.subscribe("topic");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("a");
        }
        //获得sb的字节数
        int len = sb.toString().getBytes().length;
        System.out.println("消息大小为：" + sb.toString().getBytes().length);
        //开始计时
        long startTime = System.currentTimeMillis();
        publisher.publish(sb.toString(), "topic");
        while(true) {
            String recv = subscriber.getTestContent();
            if (!recv.isEmpty()) {
                break;
            }
            System.out.print("");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总耗时为：" + (endTime - startTime) + "ms");
        System.out.println("吞吐量为：" + (len * 1000 / (endTime - startTime)) / 1000.0 + "KB/s");
        //结束所有线程
//        System.exit(0);
    }
    public void testThroughputSub() {
        //修改Config中的type
        Config.type = 3;
        Config.mqPort = 9998;
        Broker t = new Broker("1");
        new Thread(t).start();
        Entity publisher = new Entity(1, 0);
        Entity subscriber = new Entity(1, 8081);
        new Thread(subscriber).start();
        subscriber.subscribe("topic");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("a");
        }
        //获得sb的字节数
        int len = sb.toString().getBytes().length;
        System.out.println("消息大小为：" + sb.toString().getBytes().length);
        //开始计时
        long startTime = System.currentTimeMillis();
        publisher.publish(sb.toString(), "topic");
        int i = 0;
        while(true) {
            String recv = subscriber.getTestContent();
            if (!recv.isEmpty()) {
                break;
            }
            System.out.print("");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总耗时为：" + (endTime - startTime) + "ms");
        System.out.println("吞吐量为：" + (len * 1000 / (endTime - startTime)) / 1000.0 + "KB/s");
        //结束所有线程
//        System.exit(0);
    }
    public void testThroughputPiont() {
        //修改Config中的type
        Config.type = 2;
        Config.mqPort = 9997;
        Broker t = new Broker("1");
        new Thread(t).start();
        Entity publisher = new Entity(1, 0);
        Entity subscriber = new Entity(1, 8082);
        new Thread(subscriber).start();
        subscriber.subscribe("topic");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("a");
        }
        //获得sb的字节数
        int len = sb.toString().getBytes().length;
        System.out.println("消息大小为：" + sb.toString().getBytes().length);
        //开始计时
        long startTime = System.currentTimeMillis();
        publisher.publish(sb.toString(), "topic");
        subscriber.get();

        int i = 0;
        while(true) {
            String recv = subscriber.getTestContent();
            if (!recv.isEmpty()) {
                break;
            }
            System.out.print("");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总耗时为：" + (endTime - startTime) + "ms");
        System.out.println("吞吐量为：" + (len * 1000 / (endTime - startTime)) / 1000.0 + "KB/s");
        //结束所有线程
//        System.exit(0);
    }
    public static void main(String[] args) {
        throughputTest test = new throughputTest();
        System.out.println("全广播");
        test.testThroughputAll();
        System.out.println("发布订阅");
        test.testThroughputSub();
        System.out.println("点对点");
        test.testThroughputPiont();
        //结束所有线程
        System.exit(0);
    }
}

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

public class Entity implements Runnable{
    int port;
    String address;
    String mgHead;
    int id = 0;
    int entityPort;
    List topicList = new ArrayList<ArrayList>();
    public Entity(int id, int entityPort) {
        //从配置类中读取配置信息
        Config config = new Config();
        port = config.mqPort;
        address = config.address;
        this.id = id;
        this.entityPort = entityPort;
    }
    public void publish(String message, String topic) {
        //向消息队列发送消息
        try (Socket socket = new Socket(address, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
//            System.out.println("发布到端口为：" + port + "的消息队列");
            topicList.add(topic);
            oos.writeObject(new Message(Config.publish, message, id, topic, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void subscribe(String topic) {
        // 绑定到本地地址和端口
        //向消息队列发送消息
        try (Socket socket = new Socket(address, port);
             //绑定端口
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
            oos.writeObject(new Message(Config.subscribe, "", id, topic, entityPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void get() {
        //从消息队列收消息
        try (Socket socket = new Socket(address, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
            oos.writeObject(new Message(Config.get, "", id, "", entityPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void receive() {
        //从消息队列收消息
        //通过socket对消息队列发送的内容进行接收
        try (Socket socket = new Socket(address, port)) {
            //接收消息队列的消息
            ObjectInputStream ooi = new ObjectInputStream(socket.getInputStream());
            Message req = (Message) ooi.readObject();
            String topic = req.getMsgTopicName();
            if (topicList.contains(topic)) {
                System.out.println("id为：" +id + "的接收者" + "收到消息：" + req.getMsgBody());
            }
            else {
                System.out.println("id为：" +id + "的接收者" + "没有订阅该主题");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run () {
        //实现多线程
        while (true) {
            try (ServerSocket ss = new ServerSocket(entityPort);) {// ServerSocket是监听端口的类
//                System.out.println("Topic监听端口为：" + entityPort);
                while (true) {
                    Socket socket = ss.accept();
                    //获得源端口
                    //收到信息
                    Message req = (Message) new ObjectInputStream(socket.getInputStream()).readObject();
                    String Head = req.getMsgHeader();
                    if (Head.equals("WRONG")) {
                        System.out.println("    没有订阅该主题");
                        continue;
                    }
                    String data = req.getMsgBody();
                    System.out.println("    编号为：" + id + "的订阅者收到数据为：" + data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

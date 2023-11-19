import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Topic implements Runnable{
    public Map<Integer, Integer> subscriberPort = new HashMap<>();
    public Map<Integer, List<String>> subscribersMap = new HashMap<>();
    public List<Message> messages = new ArrayList<>();
    public String name;
    public int port;
    public String type;

    public Topic(String name) {
        this.name = name;
        this.port = new Config().mqPort;
    }

    public void run() {
        while (true) {
            //在端口监听消息
            try (ServerSocket ss = new ServerSocket(port);) {// ServerSocket是监听端口的类
                System.out.println("Topic监听端口为：" + port);
                while (true) {
                    Socket socket = ss.accept();
//                    Process p = new Process(socket, this);
//                    TopicProcess p = new TopicProcess(socket, pubHead, getHead, subHead, completeHead,
//                            wrongHead, updateTime);
                    process p = new process(socket, this);
                    new Thread(p).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void update() {
        //更新消息
        //清除过期消息
        for (Message message : messages) {
            //消息的时间类型为Date，如果消息的时间加上一分钟小于当前时间，则删除该消息
            if (message.getDate().getTime() + 60000 < new Date().getTime()) {
                System.out.println("删除消息的时间为：" + message.getDate().getTime() + "当前时间为：" + new Date().getTime());
                messages.remove(message);
            }
        }
    }
    public boolean updatePublisher(Message message) {
        //添加发布者
        int publishId = message.getMsgSource();
        messages.add(message);
        if (Config.type == 1) {
            //全广播模式
            for (Integer subscriberId : subscriberPort.keySet()) {
                //发送消息
                try (Socket socket = new Socket(Config.address, subscriberPort.get(subscriberId));
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                    oos.writeObject(new Message(Config.message, message.getMsgBody(), 0, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public boolean subscribe(String topicName, int subscriberId, int sourcePort, String sourceAddress) {
        //添加订阅者
        //TODO:这里传过来的sourcePort有误，需要修改
        if (! subscriberPort.containsKey(subscriberId)) {
            subscriberPort.put(subscriberId, sourcePort);
        }
        if (! subscribersMap.containsKey(subscriberId)) {
            subscribersMap.put(subscriberId, new ArrayList<String>());
        }
        subscribersMap.get(subscriberId).add(topicName);
//        for (Message message : messages) {
//            if (message.getMsgTopicName().equals(topicName)) {
//                //发送消息
//                try (Socket socket = new Socket(sourceAddress, sourcePort);
//                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
//                    oos.writeObject(new Message(Config.message, message.getMsgBody(), 0, topicName));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return true;
    }
    public boolean get(Message message, int sourcePort, String sourceAddress) {
        //获取消息
        int subscriberId = message.getMsgSource();
        String topicName = message.getMsgTopicName();
        if (! subscriberPort.containsKey(subscriberId)) {
            return false;
        }
        if (! subscribersMap.containsKey(subscriberId)) {
            return false;
        }
        if (! subscribersMap.get(subscriberId).contains(topicName)) {
            return false;
        }
        List<Message> messages = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getMsgTopicName().equals(topicName)) {
                messages.add(msg);
                //发送消息
                try (Socket socket = new Socket(sourceAddress, sourcePort);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                    oos.writeObject(new Message(Config.message, msg.getMsgBody(), 0, topicName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try (Socket socket = new Socket(sourceAddress, sourcePort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());){
            oos.writeObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}


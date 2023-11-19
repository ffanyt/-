import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Topic {
    public List<Integer> subscribers = new ArrayList<Integer>();
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
    public boolean updatePublisher(Message message) {
        //添加发布者
        int publishId = message.getMsgSource();
        if (! subscribers.contains(publishId)) {
            subscribers.add(publishId);
        }
        messages.add(message);
        return true;
    }
    public boolean subscribe(String topicName, int subscriberId, int sourcePort, String sourceAddress) {
        //添加订阅者
        if (! subscribers.contains(subscriberId)) {
            subscribers.add(subscriberId);
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
        if (! subscribers.contains(subscriberId)) {
            return false;
        }
        if (! subscribersMap.containsKey(subscriberId)) {
            return false;
        }
        if (! subscribersMap.get(subscriberId).contains(topicName)) {
            return false;
        }
        for (Message msg : messages) {
            if (msg.getMsgTopicName().equals(topicName)) {
                //发送消息
                try (Socket socket = new Socket(sourceAddress, sourcePort);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                    oos.writeObject(new Message(Config.message, msg.getMsgBody(), 0, topicName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}


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
//                System.out.println("Topic监听端口为：" + port);
                while (true) {
                    Socket socket = ss.accept();
//                    Process p = new Process(socket, this);
//                    TopicProcess p = new TopicProcess(socket, pubHead, getHead, subHead, completeHead,
//                            wrongHead, updateTime);
                    //获得源端口
                    int sourcePort = socket.getPort();
//                    System.out.println("消息来源的端口为：" + sourcePort);
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
//                int targetPort = message.getSourcePort();
                int targetPort = subscriberPort.get(subscriberId);
                //发送消息
                try (Socket socket = new Socket(Config.address, targetPort);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                    oos.writeObject(new Message(Config.message, message.getMsgBody(), 0, "", 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (Config.type == 3) {
            System.out.println("该消息为发布订阅模式，自动向订阅者发布信息");
            //获得topicName
            String topicName = message.getMsgTopicName();
            //获得订阅者列表
//            List<String> topics = subscribersMap.get(publishId);
            for (Integer subscriberId : subscriberPort.keySet()) {
                if (subscribersMap.get(subscriberId).contains(topicName)) {
                    int targetPort = subscriberPort.get(subscriberId);
                    //发送消息
                    try (Socket socket = new Socket(Config.address, targetPort);
                         ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                        oos.writeObject(new Message(Config.message, message.getMsgBody(), 0, "", 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return true;
    }
    public boolean subscribe(String topicName, int subscriberId, int sourcePort, String sourceAddress) {
        //添加订阅者
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
//        String topicName = message.getMsgTopicName();
        if (! subscriberPort.containsKey(subscriberId)) {
            return false;
        }
        if (! subscribersMap.containsKey(subscriberId)) {
            return false;
        }
        List<String> topics = subscribersMap.get(subscriberId);
//        List<Message> messages = new ArrayList<>();
        int len = messages.size();
        for (int i = len - 1; i >= 0; i--) {
            String msgTopicName = messages.get(i).getMsgTopicName();
            if (topics.contains(msgTopicName)) {
                try (Socket socket = new Socket(sourceAddress, sourcePort);
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                    oos.writeObject(messages.get(i));
                    //进入get函数意味着为点对点模式，那么在消息队列发送消息后，应该删除该消息
                    messages.remove(i);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //如果没有消息，则发送一个错误消息
        try (Socket socket = new Socket(sourceAddress, sourcePort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
            oos.writeObject(new Message(Config.wrong, "", 0, "", 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}


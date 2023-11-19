import java.io.ObjectInputStream;
import java.net.Socket;

public class process implements Runnable {
    Socket socket;
    Topic topic;

    public process(Socket socket, Topic topic) {
        //从配置类中读取配置信息
        this.socket = socket;
        this.topic = topic;
    }

    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
            Message req = (Message) ois.readObject();
            String Head = req.getMsgHeader();
            //获取消息来源的端口
            int sourcePort = socket.getPort();
            //获取消息来源的地址
            String sourceAddress = socket.getInetAddress().getHostAddress();
            switch (Head) {
                case "PUBLISH":
                    //发布消息
                    topic.update();
                    if (topic.updatePublisher(req)) {
                        System.out.println("发布成功");
                    } else {
                        System.out.println("发布失败");
                    }
                    break;
                case "SUBSCRIBE":
                    topic.update();
                    //订阅消息
                    if (topic.subscribe(req.getMsgTopicName(), req.getMsgSource(), sourcePort, sourceAddress)) {
                        System.out.println("订阅成功");
                    } else {
                        System.out.println("订阅失败");
                    }
                    break;
                case "get":
                    topic.update();
                    //获取消息
                    if (topic.get(req, sourcePort, sourceAddress)) {
                        System.out.println("获取成功");
                    } else {
                        System.out.println("获取失败");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

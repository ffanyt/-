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
                case "publish":
                    //发布消息
                    if (topic.updatePublisher(req)) {
                        System.out.println("发布成功");
                    } else {
                        System.out.println("发布失败");
                    }
                    if (Config.type == 1)
                        //TODO 收完数据如何处理，全广则直接发送全部订阅者
                    break;
                case "subscribe":
                    //订阅消息
                    if (topic.subscribe(req.getMsgTopicName(), req.getMsgSource(), sourcePort, sourceAddress)) {
                        System.out.println("订阅成功");
                    } else {
                        System.out.println("订阅失败");
                    }
                    //TODO 在订阅者收的时候，只收了一个Message，但是消息队列是把所有的Message发送过去的，所以这里需要修改
                    //TODO 发和收都用List<Message>，这样就可以一次性发送所有的Message
                    //TODO Message可能要implement Serializable
                    break;
                case "get":
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

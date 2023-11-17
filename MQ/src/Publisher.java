import com.sun.net.httpserver.Request;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Properties;

public class Publisher {
    int port;
    String address;
    String mgHead;
    public Publisher() {
        //从配置类中读取配置信息
        Config config = new Config();
        port = config.mqPort;
        address = config.address;
        mgHead = config.publish;
    }
    public void publisher(String message) {
        //向消息队列发送消息
        try (Socket socket = new Socket(address, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
            oos.writeObject(new Message(mgHead, message, "publisher"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

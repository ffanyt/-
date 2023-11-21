import java.util.*;

public class Main {
    public static void main(String[] args) {

//            System.out.println("选择广播模式，1为全广播式，2为选择广播-点对点，3为选择广播-发布订阅");
//            Scanner sc = new Scanner(System.in);
//            // 错误处理
//            String typeIn = sc.nextLine();
//            if (typeIn.equals("1") || typeIn.equals("2") || typeIn.equals("3")) {
//                System.out.println("输入正确");
//            } else {
//                System.out.println("输入错误，请重新输入");
//                continue;
//            }
//            int type = Integer.parseInt(typeIn);
        System.out.println("从配置文件读取模式信息...");
        int type = Config.type;
        Broker t = new Broker("1");
        new Thread(t).start();
        switch (type) {
            case 1:
                System.out.println("进入全广播式");
                System.out.println("输入发布者id:");
                Scanner sc = new Scanner(System.in);
                //int
                String publisherId = sc.nextLine();
                int id = Integer.parseInt(publisherId);
                Entity publisher = new Entity(id, 0);
                System.out.println("输入订阅者数量:");
                String subscriberNum = sc.nextLine();
                int num = Integer.parseInt(subscriberNum);
                for (int i = 0; i < num; i++) {
                    System.out.println("输入订阅者id:");
                    String subscriberId = sc.nextLine();
                    int subId = Integer.parseInt(subscriberId);
                    System.out.println("输入订阅者端口:");
                    String subscriberPort = sc.nextLine();
                    int subPort = Integer.parseInt(subscriberPort);
                    Entity subscriber = new Entity(subId, subPort);
                    new Thread(subscriber).start();
                    System.out.println("输入消息主题：");
                    String topic = sc.nextLine();
                    subscriber.subscribe(topic);
                }
                while (true) {
//                    System.out.println("1");
                    System.out.println("输入消息:");
                    Scanner sc1 = new Scanner(System.in);
                    String message = sc1.nextLine();
                    System.out.println("输入消息主题：");
                    String topic = sc1.nextLine();
                    publisher.publish(message, topic);
//                    System.out.println("2");
                }
//                System.out.println("输入消息:");
//                Scanner sc1 = new Scanner(System.in);
//                String message = sc1.nextLine();
//                publisher.publish(message, "1");

//                break;
            case 2:
                System.out.println("进入广播-点对点");
                System.out.println("输入发布者id:");
                Scanner sc1 = new Scanner(System.in);
                //int
                String publisherId1 = sc1.nextLine();
                int id1 = Integer.parseInt(publisherId1);
                Entity publisher1 = new Entity(id1, 0);
                System.out.println("输入订阅者数量:");
                String subscriberNum1 = sc1.nextLine();
                int num1 = Integer.parseInt(subscriberNum1);
                Map<Integer, Entity> integerEntityHashMap = new HashMap<>();
                for (int i = 0; i < num1; i++) {
                    System.out.println("输入订阅者id:");
                    String subscriberId = sc1.nextLine();
                    int subId = Integer.parseInt(subscriberId);
                    if (integerEntityHashMap.containsKey(subId)) {
                        System.out.println("该编号已经被使用，请重新输入");
                        i--;
                        continue;
                    }
                    System.out.println("输入订阅者端口:");
                    String subscriberPort = sc1.nextLine();
                    int subPort = Integer.parseInt(subscriberPort);
                    Entity subscriber = new Entity(subId, subPort);
                    new Thread(subscriber).start();
                    System.out.println("输入消息主题：");
                    String topic = sc1.nextLine();
                    integerEntityHashMap.put(subId, subscriber);
                    subscriber.subscribe(topic);
                }
                System.out.println("输入消息:");
                Scanner sc2 = new Scanner(System.in);
                String message = sc2.nextLine();
                System.out.println("输入消息主题：");
                String topic = sc2.nextLine();
                publisher1.publish(message, topic);
                while (true) {
                    System.out.println("你要使用什么订阅者进行接收，请输入ID:");
                    Scanner sc3 = new Scanner(System.in);
                    String message1 = sc3.nextLine();
                    int id2 = Integer.parseInt(message1);
                    if (!integerEntityHashMap.containsKey(id2)) {
                        System.out.println("该编号不存在，请重新输入");
                        continue;
                    }
                    Entity entity = integerEntityHashMap.get(id2);
                    entity.get();
                }
//                break;
            case 3:
                System.out.println("进入广播-发布订阅");
                System.out.println("输入发布者id:");
                Scanner sc_model_3 = new Scanner(System.in);
                //int
                String publisherId2 = sc_model_3.nextLine();
                int id2 = Integer.parseInt(publisherId2);
                Entity publisher2 = new Entity(id2, 0);
                System.out.println("输入订阅者数量:");
                String subscriberNum2 = sc_model_3.nextLine();
                int num_model3 = Integer.parseInt(subscriberNum2);
                Map<Integer, Entity> integerEntityHashMap1 = new HashMap<>();
                for (int i = 0; i < num_model3; i++) {
                    System.out.println("输入订阅者id:");
                    String subscriberId = sc_model_3.nextLine();
                    int subId = Integer.parseInt(subscriberId);
                    if (integerEntityHashMap1.containsKey(subId)) {
                        System.out.println("该编号已经被使用，请重新输入");
                        i--;
                        continue;
                    }
                    System.out.println("输入订阅者端口:");
                    String subscriberPort = sc_model_3.nextLine();
                    int subPort = Integer.parseInt(subscriberPort);
                    Entity subscriber = new Entity(subId, subPort);
                    new Thread(subscriber).start();
                    System.out.println("输入消息主题：");
                    String topic1 = sc_model_3.nextLine();
                    integerEntityHashMap1.put(subId, subscriber);
                    subscriber.subscribe(topic1);
                }
                while (true) {
                    System.out.println("输入消息:");
                    String message1 = sc_model_3.nextLine();
                    System.out.println("输入消息主题：");
                    String topic1 = sc_model_3.nextLine();
                    publisher2.publish(message1, topic1);
                }
//                break;
            default:
                System.out.println("输入错误，请重新输入");
            }
        }
}

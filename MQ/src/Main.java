import java.util.Scanner;

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
        Topic t = new Topic("1");
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
                    subscriber.subscribe("1");
                }
                System.out.println("输入消息:");
                Scanner sc1 = new Scanner(System.in);
                String message = sc1.nextLine();
                publisher.publish(message, "1");

                break;
            case 2:
                System.out.println("进入广播-点对点");
                break;
            case 3:
                System.out.println("进入广播-发布订阅");
                break;
            default:
                System.out.println("输入错误，请重新输入");
            }
        }
}

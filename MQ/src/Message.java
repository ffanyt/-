public class Message {
    private String msgHeader;
    private String msgBody;
    private int msgSource;
    private String msgTopicName;

    public Message(String msgHeader, String msgBody, int msgSource, String msgTopicName) {
        this.msgHeader = msgHeader;
        this.msgBody = msgBody;
        this.msgSource = msgSource;
        this.msgTopicName = msgTopicName;
    }

    public String getMsgHeader() {
        return msgHeader;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public int getMsgSource() {
        return msgSource;
    }

    public String getMsgTopicName() {
        return msgTopicName;
    }
}

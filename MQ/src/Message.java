public class Message {
    private String msgHeader;
    private String msgBody;
    private String msgSource;

    public Message(String msgHeader, String msgBody, String msgSource) {
        this.msgHeader = msgHeader;
        this.msgBody = msgBody;
        this.msgSource = msgSource;
    }

    public String getMsgHeader() {
        return msgHeader;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getMsgSource() {
        return msgSource;
    }
}

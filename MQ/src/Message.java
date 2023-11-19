import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String msgHeader;
    private String msgBody;
    private int msgSource;
    private String msgTopicName;
    private Date date;

    public Message(String msgHeader, String msgBody, int msgSource, String msgTopicName) {
        this.msgHeader = msgHeader;
        this.msgBody = msgBody;
        this.msgSource = msgSource;
        this.msgTopicName = msgTopicName;
        date = new Date();
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

    public Date getDate() {
        return date;
    }
}

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String msgHeader;
    private String msgBody;
    private int msgSource;
    private String msgTopicName;
    private Date date;
    private int sourcePort;

    public Message(String msgHeader, String msgBody, int msgSource, String msgTopicName, int sourcePort) {
        this.msgHeader = msgHeader;
        this.msgBody = msgBody;
        this.msgSource = msgSource;
        this.msgTopicName = msgTopicName;
        date = new Date();
        this.sourcePort = sourcePort;
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

    public int getSourcePort() {
        return sourcePort;
    }
}

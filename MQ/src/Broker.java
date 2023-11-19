import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker {
    public Map<Integer, List<String>> subscribeTopicName = new HashMap<Integer, List<String>>();
    public List<Integer> subscribers = new ArrayList<Integer>();
    public List<Integer> publishers = new ArrayList<Integer>();
    public List<Topic> topics = new ArrayList<Topic>();

    public Broker() {
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }


}

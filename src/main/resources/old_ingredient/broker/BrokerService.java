package old_ingredient.broker;

public interface BrokerService {

    void sendMessage(String routing_key, String message);

}

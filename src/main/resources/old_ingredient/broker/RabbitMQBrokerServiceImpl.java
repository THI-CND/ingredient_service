package old_ingredient.broker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQBrokerServiceImpl implements BrokerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchangeName;

    @Autowired
    public RabbitMQBrokerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String routing_key, String message) {
        rabbitTemplate.convertAndSend(topicExchangeName, routing_key, message);
    }

}

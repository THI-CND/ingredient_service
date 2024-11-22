package de.thi.cnd.adapter.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchangeName;

    @Autowired
    public MessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String routing_key, String message) {
        rabbitTemplate.convertAndSend(topicExchangeName, routing_key, message);
    }

}
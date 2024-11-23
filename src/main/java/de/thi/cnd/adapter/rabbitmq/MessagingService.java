package de.thi.cnd.adapter.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void publish(String routingKey, Object object) {
        String content = asJsonString(object);
        try {
            rabbitTemplate.convertAndSend(topicExchangeName, routingKey, content);
        } catch (Exception e) {
            System.out.println("Could not send RabbitMQ message: " + e.getMessage());
        }
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
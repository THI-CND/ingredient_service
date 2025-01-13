package de.thi.cnd.adapter.outgoing.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchangeName;

    public MessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, Object object) {
        String content = asJsonString(object);
        try {
            rabbitTemplate.convertAndSend(topicExchangeName, routingKey, content);
        } catch (Exception e) {
            logger.error("Error publishing message", e);
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
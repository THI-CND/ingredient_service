package de.thi.cnd.ingredient.broker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQBrokerServiceImpl implements BrokerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQBrokerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String routing_key, String message) {
        rabbitTemplate.convertAndSend("cnd.ingredients_exchange", routing_key, message);
    }

}

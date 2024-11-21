package de.thi.cnd.adapter.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final TopicExchange exchange;

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchangeName;

    @Value("${app.message.queue.ingredients.name}")
    private String queueName;

    @Value("${app.message.queue.ingredients.routing.created}")
    private String routingKeyCreated;

    @Value("${app.message.queue.ingredients.routing.updated}")
    private String routingKeyUpdated;

    @Value("${app.message.queue.ingredients.routing.deleted}")
    private String routingKeyDeleted;

    @Value("${app.message.queue.ingredients.routing.tag}")
    private String routingKeyTag;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder.bind(queue).to(exchange).with(routingKeyCreated),
                BindingBuilder.bind(queue).to(exchange).with(routingKeyUpdated),
                BindingBuilder.bind(queue).to(exchange).with(routingKeyDeleted),
                BindingBuilder.bind(queue).to(exchange).with(routingKeyTag)
        );
    }

    @Autowired
    public MessagingService(RabbitTemplate rabbitTemplate, Queue queue, TopicExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.exchange = exchange;
    }

    public void sendMessage(String routing_key, String message) {
        rabbitTemplate.convertAndSend(topicExchangeName, routing_key, message);
    }

}

package de.thi.cnd.adapter.outgoing.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${app.message.queue.ingredients.name}")
    private String queueName;

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchangeName;

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
                BindingBuilder.bind(queue()).to(exchange()).with(routingKeyCreated),
                BindingBuilder.bind(queue()).to(exchange()).with(routingKeyUpdated),
                BindingBuilder.bind(queue()).to(exchange()).with(routingKeyDeleted),
                BindingBuilder.bind(queue()).to(exchange()).with(routingKeyTag)
        );
    }
}

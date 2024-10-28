package de.thi.cnd;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IngredientApplication {
    //TODO: Konfigurierbar machen
    static final String topicExchangeName = "cnd.ingredients_exchange";

    static final String queueName = "ingredients";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /*@Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange()).with("ingredients_routing_key");

    }*/

    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder.bind(queue()).to(exchange()).with("ingredients.created"),
                BindingBuilder.bind(queue()).to(exchange()).with("ingredients.deleted")
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(IngredientApplication.class, args);
    }

}
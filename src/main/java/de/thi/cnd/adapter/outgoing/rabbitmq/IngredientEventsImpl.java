package de.thi.cnd.adapter.outgoing.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientCreatedEventDTO;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientDeletedEventDTO;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientUpdatedEventDTO;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.TagCreatedEventDTO;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IngredientEventsImpl implements IngredientEvents {

    private static final Logger logger = LoggerFactory.getLogger(IngredientEventsImpl.class);

    @Value("${app.message.queue.ingredients.exchange}")
    private String topicExchange;

    private final RabbitTemplate rabbitTemplate;

    public IngredientEventsImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.message.queue.ingredients.routing.created}")
    private String routingKeyCreated;

    @Value("${app.message.queue.ingredients.routing.updated}")
    private String routingKeyUpdated;

    @Value("${app.message.queue.ingredients.routing.deleted}")
    private String routingKeyDeleted;

    @Value("${app.message.queue.ingredients.routing.tag}")
    private String routingKeyTag;

    @Override
    public void ingredientCreated(Ingredient ingredient) {
        IngredientCreatedEventDTO ingredientCreatedEventDTO = new IngredientCreatedEventDTO(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags());
        publishEvent(ingredientCreatedEventDTO, routingKeyCreated);
    }

    @Override
    public void ingredientUpdated(Ingredient ingredient) {
        IngredientUpdatedEventDTO ingredientUpdatedEventDTO = new IngredientUpdatedEventDTO(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags());
        publishEvent(ingredientUpdatedEventDTO, routingKeyUpdated);
    }

    @Override
    public void ingredientDeleted(Ingredient ingredient) {
        IngredientDeletedEventDTO ingredientDeletedEventDTO = new IngredientDeletedEventDTO(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags());
        publishEvent(ingredientDeletedEventDTO, routingKeyDeleted);
    }

    @Override
    public void tagCreated(String name) {
        TagCreatedEventDTO tagCreatedEventDTO = new TagCreatedEventDTO(name);
        publishEvent(tagCreatedEventDTO, routingKeyTag);
    }

    private void publishEvent(Object event, String routingKey) {
        String content = asJsonString(event);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        Message message = new Message(content.getBytes(), messageProperties);

        try {
            rabbitTemplate.convertAndSend(topicExchange, routingKey, message);
        } catch (Exception e) {
            logger.error("Error publishing AMQP message", e);
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

package de.thi.cnd.adapter.outgoing.rabbitmq;

import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientCreatedEvent;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientDeletedEvent;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.IngredientUpdatedEvent;
import de.thi.cnd.adapter.outgoing.rabbitmq.dto.TagCreatedEvent;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IngredientEventsImpl implements IngredientEvents {

    @Autowired
    private MessagingService messagingService;

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
        messagingService.publish(routingKeyCreated, new IngredientCreatedEvent(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags()));
    }

    @Override
    public void ingredientUpdated(Ingredient ingredient) {
        messagingService.publish(routingKeyUpdated, new IngredientUpdatedEvent(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags()));
    }

    @Override
    public void ingredientDeleted(Ingredient ingredient) {
        messagingService.publish(routingKeyDeleted, new IngredientDeletedEvent(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getTags()));
    }

    @Override
    public void tagCreated(String name) {
        messagingService.publish(routingKeyTag, new TagCreatedEvent(name));
    }

}

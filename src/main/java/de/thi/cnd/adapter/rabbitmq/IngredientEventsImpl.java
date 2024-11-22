package de.thi.cnd.adapter.rabbitmq;

import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientEventsImpl implements IngredientEvents {

    @Autowired
    private MessagingService messagingService;

    @Override
    public void ingredientCreated(Ingredient ingredient) {
        messagingService.sendMessage("ingredients.created", ingredient.toString());
    }

    @Override
    public void ingredientUpdated(Ingredient ingredient) {
        messagingService.sendMessage("ingredients.updated", ingredient.toString());
    }

    @Override
    public void ingredientDeleted(Ingredient ingredient) {
        messagingService.sendMessage("ingredients.deleted", ingredient.toString());
    }

    @Override
    public void tagCreated(String name) {
        messagingService.sendMessage("ingredients.tags.created", name);
    }

}

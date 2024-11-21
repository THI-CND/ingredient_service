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
        messagingService.sendMessage("ingredient.created", ingredient.toString());
    }

}

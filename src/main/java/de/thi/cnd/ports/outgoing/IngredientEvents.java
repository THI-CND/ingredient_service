package de.thi.cnd.ports.outgoing;

import de.thi.cnd.domain.model.Ingredient;

public interface IngredientEvents {

    void ingredientCreated(Ingredient ingredient);

    void ingredientUpdated(Ingredient ingredient);

    void ingredientDeleted(Ingredient ingredient);

    void tagCreated(String name);

}

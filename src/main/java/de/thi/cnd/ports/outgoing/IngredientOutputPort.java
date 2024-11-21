package de.thi.cnd.ports.outgoing;

import de.thi.cnd.domain.model.Ingredient;

import java.util.List;

public interface IngredientOutputPort {

    Ingredient saveIngredient(Ingredient ingredient);

    List<Ingredient> getIngredients();

    Ingredient getIngredientById(Long id);

    Ingredient updateIngredient(Long ingredientId, String name, String unit, List<String> tags);

    void deleteIngredient(Long id);

    List<String> getTags();

    List<Ingredient> getIngredientsByTag(String tag);

}

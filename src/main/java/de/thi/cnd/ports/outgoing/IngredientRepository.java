package de.thi.cnd.ports.outgoing;

import de.thi.cnd.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {

    Ingredient saveIngredient(Ingredient ingredient);

    List<Ingredient> getIngredients();

    Optional<Ingredient> getIngredientById(Long id);

    Optional<Ingredient> getIngredientByName(String name);

    Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags);

    void deleteIngredient(Long id);

    List<String> getTags();

    List<Ingredient> getIngredientsByTag(String tag);

}

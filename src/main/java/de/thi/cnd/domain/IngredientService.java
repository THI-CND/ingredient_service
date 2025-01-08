package de.thi.cnd.domain;

import de.thi.cnd.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientService {

    Ingredient createIngredient(String name, String unit, List<String> tags);

    List<Ingredient> getIngredients();

    Optional<Ingredient> getIngredientById(Long ingredientId);

    Optional<Ingredient> getIngredientByName(String name);

    Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags);

    void deleteIngredient(Long ingredientId);

    List<String> getTags();

    List<Ingredient> getIngredientsByTag(String tag);
}

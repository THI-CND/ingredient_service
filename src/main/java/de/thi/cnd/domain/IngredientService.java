package de.thi.cnd.domain;

import de.thi.cnd.domain.model.Ingredient;

import java.util.List;
import java.util.Set;

public interface IngredientService {

    Ingredient createIngredient(String name, String unit, List<String> tags);

    List<Ingredient> listIngredients();

    Ingredient getIngredientById(Long ingredientId);

    Ingredient updateIngredient(Long ingredientId, Ingredient updatedIngredient);

    void deleteIngredient(Long ingredientId);

    //List<Ingredient> getIngredientsByTag(String tag);

    //Set<String> getAllTags();
}

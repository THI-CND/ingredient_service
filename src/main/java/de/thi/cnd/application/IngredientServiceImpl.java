package de.thi.cnd.application;

import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientOutputPort ingredients;

    @Override
    public Ingredient createIngredient(String name, String unit, List<String> tags) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setTags(tags);

        return ingredients.saveIngredient(ingredient);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients.getIngredients();
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        ingredients.deleteIngredient(ingredientId);
    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) {
        return ingredients.getIngredientById(ingredientId);
    }

    @Override
    public Ingredient updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        return ingredients.updateIngredient(ingredientId, name, unit, tags);
    }

}

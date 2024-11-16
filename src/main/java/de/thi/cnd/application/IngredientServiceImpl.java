package de.thi.cnd.application;

import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        ingredients.save(ingredient);
        return ingredient;
    }

    @Override
    public List<Ingredient> listIngredients() {
        return ingredients.listAll();
    }

    @Override
    public void deleteIngredient(Long ingredientId) {

    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) {
        return null;
    }

    @Override
    public Ingredient updateIngredient(Long ingredientId, Ingredient updatedIngredient) {
        return null;
    }

}

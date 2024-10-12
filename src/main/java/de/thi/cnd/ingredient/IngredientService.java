package de.thi.cnd.ingredient;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        Optional<Ingredient> ingredientByName = ingredientRepository.findIngredientByName(ingredient.getName());
        if (ingredientByName.isPresent()) {
            throw new IllegalStateException("Ingredient existiert bereits");
        }
        ingredientRepository.save(ingredient);
        return ingredient;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public void deleteIngredient(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new IllegalStateException("Ingredient mit ID '" + ingredientId + "' existiert nicht");
        }
        ingredientRepository.deleteById(ingredientId);
    }

    public Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalStateException("Ingredient mit ID '" + ingredientId + "' existiert nicht"));
    }

    public List<Ingredient> getIngredientsByTag(String tag) {
        List<Ingredient> ingredients = ingredientRepository.findByTagsContaining(tag);

        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Keine Ingredients mit dem Tag '" + tag + "' gefunden");
        }

        return ingredients;
    }

    @Transactional
    public void updateIngredient(Long ingredientId, String name, String unit) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (name != null) {
            Optional<Ingredient> ingredientByName = ingredientRepository.findIngredientByName(name);
            if (ingredientByName.isPresent()) {
                throw new IllegalStateException("Ingredient existiert bereits");
            }
            ingredient.setName(name);
        }
        if (unit != null) {
            ingredient.setUnit(unit);
        }
    }
}

package de.thi.cnd.ingredient;

import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Ingredient mit ID '" + ingredientId + "' existiert nicht"));
    }

    public List<Ingredient> getIngredientsByTag(String tag) {
        List<Ingredient> ingredients = ingredientRepository.findByTagsContaining(tag);

        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Keine Ingredients mit dem Tag '" + tag + "' gefunden");
        }

        return ingredients;
    }

    @Transactional
    public Ingredient updateIngredient(Long ingredientId, Ingredient updatedIngredient) {
        Ingredient ingredient = getIngredientById(ingredientId);

        if (updatedIngredient.getName() != null) {
            Optional<Ingredient> ingredientByName = ingredientRepository.findIngredientByName(updatedIngredient.getName());
            if (ingredientByName.isPresent()) {
                throw new IllegalStateException("Ingredient existiert bereits");
            }
            ingredient.setName(updatedIngredient.getName());
        }
        if (updatedIngredient.getUnit() != null) {
            ingredient.setUnit(updatedIngredient.getUnit());
        }
        if (updatedIngredient.getTags() != null) {
            ingredient.setTags(updatedIngredient.getTags());
        }
        return ingredient;
    }

}

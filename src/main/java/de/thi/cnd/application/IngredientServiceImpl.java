package de.thi.cnd.application;

import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientOutputPort ingredients;

    @Autowired
    private IngredientEvents events;

    @Override
    public Ingredient createIngredient(String name, String unit, List<String> tags) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setTags(tags);

        checkForNewTags(tags, getAllTags());

        ingredients.saveIngredient(ingredient);
        events.ingredientCreated(ingredient);

        return ingredient;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients.getIngredients();
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        Ingredient deletedIngredient = ingredients.getIngredientById(ingredientId);
        events.ingredientDeleted(deletedIngredient);
        ingredients.deleteIngredient(ingredientId);
    }

    @Override
    public List<String> getTags() {
        return ingredients.getTags();
    }

    @Override
    public List<Ingredient> getIngredientsByTag(String tag) {
        return ingredients.getIngredientsByTag(tag);
    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) {
        return ingredients.getIngredientById(ingredientId);
    }

    @Override
    public Ingredient updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        checkForNewTags(tags, getAllTags());

        Ingredient updatedIngredient = ingredients.updateIngredient(ingredientId, name, unit, tags);
        events.ingredientUpdated(updatedIngredient);
        return updatedIngredient;
    }


    public Set<String> getAllTags() {
        return ingredients.getTags().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private void checkForNewTags(List<String> newTags, Set<String> existingTags) {
        List<String> addedTags = newTags.stream()
                .filter(tag -> !existingTags.contains(tag.toLowerCase())) // Prüft, ob das Tag noch nicht existiert
                .toList();

        addedTags.forEach(tag -> {
            try {
                System.out.println("NEW TAG CREATED " + tag);
                events.tagCreated(tag);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

}

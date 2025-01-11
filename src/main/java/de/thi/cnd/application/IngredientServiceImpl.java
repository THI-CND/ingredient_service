package de.thi.cnd.application;

import de.thi.cnd.adapter.outgoing.jpa.IngredientRepository;
import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientOutputPort ingredients;

    @Autowired
    private IngredientEvents events;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public Ingredient createIngredient(String name, String unit, List<String> tags) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setTags(tags);

        checkForNewTags(tags, getAllTags());

        Ingredient savedIngredient = ingredients.saveIngredient(ingredient);
        events.ingredientCreated(savedIngredient);

        return savedIngredient;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients.getIngredients();
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        Optional<Ingredient> ingredient = ingredients.getIngredientById(ingredientId);
        if(ingredient.isPresent()) {
            ingredients.deleteIngredient(ingredientId);
            events.ingredientDeleted(ingredient.get());
        }
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
    public Optional<Ingredient> getIngredientById(Long ingredientId) {
        return ingredients.getIngredientById(ingredientId);
    }

    @Override
    public Optional<Ingredient> getIngredientByName(String name) {
        return ingredients.getIngredientByName(name);
    }

    @Override
    public Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        Optional<Ingredient> ingredient = ingredients.getIngredientById(ingredientId);
        if(ingredient.isEmpty()) {
            return Optional.empty();
        }

        checkForNewTags(tags, getAllTags());

        Optional<Ingredient> updatedIngredient = ingredients.updateIngredient(ingredientId, name, unit, tags);
        events.ingredientUpdated(updatedIngredient.get());

        return updatedIngredient;
    }

    public Set<String> getAllTags() {
        return ingredients.getTags().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private void checkForNewTags(List<String> newTags, Set<String> existingTags) {
        if(newTags == null) {
            return;
        }

        List<String> addedTags = newTags.stream()
                .filter(tag -> !existingTags.contains(tag.toLowerCase())) // Prüft, ob das Tag noch nicht existiert
                .toList();

        addedTags.forEach(tag -> {
            try {
                events.tagCreated(tag.toLowerCase());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

}

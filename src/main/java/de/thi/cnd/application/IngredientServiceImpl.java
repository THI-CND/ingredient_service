package de.thi.cnd.application;

import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientEvents;
import de.thi.cnd.ports.outgoing.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientsRepository;
    private final IngredientEvents ingredientEvents;

    public IngredientServiceImpl(IngredientRepository ingredientsRepository, IngredientEvents ingredientEvents) {
        this.ingredientsRepository = ingredientsRepository;
        this.ingredientEvents = ingredientEvents;
    }

    @Override
    public Ingredient createIngredient(String name, String unit, List<String> tags) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setTags(tags);

        checkForNewTags(tags, getAllTags());

        Ingredient savedIngredient = ingredientsRepository.saveIngredient(ingredient);
        ingredientEvents.ingredientCreated(savedIngredient);

        return savedIngredient;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredientsRepository.getIngredients();
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        Optional<Ingredient> ingredient = ingredientsRepository.getIngredientById(ingredientId);
        if(ingredient.isPresent()) {
            ingredientsRepository.deleteIngredient(ingredientId);
            ingredientEvents.ingredientDeleted(ingredient.get());
        }
    }

    @Override
    public List<String> getTags() {
        return ingredientsRepository.getTags();
    }

    @Override
    public List<Ingredient> getIngredientsByTag(String tag) {
        return ingredientsRepository.getIngredientsByTag(tag);
    }

    @Override
    public Optional<Ingredient> getIngredientById(Long ingredientId) {
        return ingredientsRepository.getIngredientById(ingredientId);
    }

    @Override
    public Optional<Ingredient> getIngredientByName(String name) {
        return ingredientsRepository.getIngredientByName(name);
    }

    @Override
    public Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        Optional<Ingredient> ingredient = ingredientsRepository.getIngredientById(ingredientId);
        if(ingredient.isEmpty()) {
            return Optional.empty();
        }

        checkForNewTags(tags, getAllTags());

        Optional<Ingredient> updatedIngredient = ingredientsRepository.updateIngredient(ingredientId, name, unit, tags);
        if (updatedIngredient.isPresent()) {
            ingredientEvents.ingredientUpdated(updatedIngredient.get());
        }

        return updatedIngredient;
    }

    public Set<String> getAllTags() {
        return ingredientsRepository.getTags().stream()
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
                ingredientEvents.tagCreated(tag.toLowerCase());
            } catch (Exception ex) {
                logger.error("Error while publising tag created event for tag: " + tag, ex);
            }
        });
    }

}

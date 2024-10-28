package de.thi.cnd.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private RabbitTemplate rabbitTemplate;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(RabbitTemplate rabbitTemplate, IngredientRepository ingredientRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        Optional<Ingredient> ingredientByName = ingredientRepository.findIngredientByName(ingredient.getName());
        if (ingredientByName.isPresent()) {
            throw new IllegalStateException("Ingredient existiert bereits");
        }

        // Alle Tags toLowerCase
        ingredient.setTags(ingredient.getTags().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList()));

        try {
            String ingredientJson = new ObjectMapper().writeValueAsString(ingredient);
            this.sendMessage("ingredients.created", ingredientJson);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        ingredientRepository.save(ingredient);
        return ingredient;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public void deleteIngredient(Long ingredientId) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (!ingredientOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found");
        }
        Ingredient ingredient = ingredientOpt.get();

        try {
            String ingredientJson = new ObjectMapper().writeValueAsString(ingredient);
            this.sendMessage("ingredients.deleted", ingredientJson);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        ingredientRepository.deleteById(ingredientId);
    }

    public Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));
    }

    public List<Ingredient> getIngredientsByTag(String tag) {
        List<Ingredient> ingredients = ingredientRepository.findByTagsContaining(tag);

        if (ingredients.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ingredients found");
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

    public void sendMessage(String routing_key, String message) {
        rabbitTemplate.convertAndSend("cnd.ingredients_exchange", routing_key, message);
    }

}

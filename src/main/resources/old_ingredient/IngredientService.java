package old_ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thi.cnd.old_ingredient.broker.BrokerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private BrokerService brokerService;
    private final IngredientRepository ingredientRepository;

    @Value("${app.message.queue.ingredients.routing.created}")
    private String routingKeyCreated;

    @Value("${app.message.queue.ingredients.routing.updated}")
    private String routingKeyUpdated;

    @Value("${app.message.queue.ingredients.routing.deleted}")
    private String routingKeyDeleted;

    @Value("${app.message.queue.ingredients.routing.tag}")
    private String routingKeyTag;

    @Autowired
    public IngredientService(BrokerService brokerService, IngredientRepository ingredientRepository) {
        this.brokerService = brokerService;
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        Optional<Ingredient> ingredientByName = ingredientRepository.findIngredientByName(ingredient.getName());
        if (ingredientByName.isPresent()) {
            throw new IllegalStateException("Ingredient existiert bereits");
        }

        // Alle Tags toLowerCase
        List<String> tags = ingredient.getTags().stream()
                .map(String::toLowerCase)
                .toList();
        ingredient.setTags(tags);

        checkForNewTags(tags, getAllTags());

        try {
            String ingredientJson = new ObjectMapper().writeValueAsString(ingredient);
            brokerService.sendMessage(routingKeyCreated, ingredientJson);
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
            brokerService.sendMessage(routingKeyDeleted, ingredientJson);
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
        Set<String> existingTags = getAllTags();

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
            List<String> newTags = updatedIngredient.getTags().stream()
                    .map(String::toLowerCase)
                    .toList();
            ingredient.setTags(newTags);

            checkForNewTags(newTags, existingTags);
        }

        try {
            String ingredientJson = new ObjectMapper().writeValueAsString(ingredient);
            brokerService.sendMessage(routingKeyUpdated, ingredientJson);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return ingredient;
    }

    public Set<String> getAllTags() {
        return ingredientRepository.findAll().stream()
                .flatMap(ingredient -> ingredient.getTags().stream())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private void checkForNewTags(List<String> newTags, Set<String> existingTags) {
        List<String> addedTags = newTags.stream()
                .filter(tag -> !existingTags.contains(tag)) // Prüft, ob das Tag noch nicht existiert
                .toList();

        addedTags.forEach(tag -> {
            try {
                brokerService.sendMessage(routingKeyTag, tag);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}
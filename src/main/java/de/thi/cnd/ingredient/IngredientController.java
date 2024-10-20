package de.thi.cnd.ingredient;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/ingredients")
public class IngredientController {

    private RabbitTemplate rabbitTemplate;
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(RabbitTemplate rabbitTemplate, IngredientService ingredientService) {
        this.rabbitTemplate = rabbitTemplate;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> getIngredients(@RequestParam(value = "tag", required = false) String tag) {
        if (tag != null && !tag.isEmpty()) {
            return ingredientService.getIngredientsByTag(tag);
        } else {
            return ingredientService.getIngredients();
        }
    }

   /* @GetMapping
    public List<Ingredient> getIngredients() {
        return ingredientService.getIngredients();
    }*/

    @GetMapping("{ingredientId}")
    public Ingredient getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        this.sendMessage(ingredientService.getIngredientById(ingredientId).getName());
        return ingredientService.getIngredientById(ingredientId);
    }

    // Nur /tags geht nicht, da sonst "tags" als ingredientId gesehen wird - Eigenen Controller etc. für Tags schreiben?
    @GetMapping("/tags/all")
    public List<String> getTags() {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        return ingredients.stream()
                .flatMap(ingredient -> ingredient.getTags().stream())  // Alle Tags jedes Ingredients
                .distinct()  // Duplikate entfernen
                .collect(Collectors.toList());
    }

    /*@GetMapping("/tags/{tag}")
    public List<Ingredient> getIngredientsByTag(@PathVariable("tag") String tag) {
        return ingredientService.getIngredientsByTag(tag);
    }*/

    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {
        Ingredient savedIngredient = ingredientService.addIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
    }

    @PutMapping("{ingredientId}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable("ingredientId") Long ingredientId,
            @RequestBody Ingredient updatedIngredient) {

        try {
            Ingredient savedIngredient = ingredientService.updateIngredient(ingredientId, updatedIngredient);
            return ResponseEntity.ok(savedIngredient);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("{ingredientId}")
    public void deleteIngredient(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("ingredients_exchange", "ingredients_routing_key", message);
    }

}

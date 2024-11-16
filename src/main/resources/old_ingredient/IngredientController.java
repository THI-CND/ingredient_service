package old_ingredient;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
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

    @GetMapping("{ingredientId}")
    public Ingredient getIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        return ingredientService.getIngredientById(ingredientId);
    }

    @GetMapping("/tags")
    public Set<String> getTags() {
        return ingredientService.getAllTags();
    }

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

}

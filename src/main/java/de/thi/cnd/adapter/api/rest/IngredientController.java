package de.thi.cnd.adapter.api.rest;

import de.thi.cnd.adapter.api.rest.dto.CreateIngredientRequest;
import de.thi.cnd.adapter.api.rest.dto.IngredientResponse;
import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService service;

    @PostMapping
    public IngredientResponse createIngredient(@RequestBody CreateIngredientRequest request) {
        Ingredient i = service.createIngredient(request.getName(), request.getUnit(), request.getTags());
        return new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags());
    }

    @GetMapping
    public List<IngredientResponse> getIngredients(@RequestParam(value = "tag", required = false) String tag) {
        if (tag != null && !tag.isEmpty()) {
            List<Ingredient> list = service.getIngredientsByTag(tag);
            List<IngredientResponse> ingredients = new ArrayList<>();

            for (Ingredient i : list) {
                ingredients.add(new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags()));
            }
            return ingredients;
        } else {
            List<Ingredient> list = service.getIngredients();
            List<IngredientResponse> ingredients = new ArrayList<>();

            for (Ingredient i : list) {
                ingredients.add(new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags()));
            }
            return ingredients;
        }
    }

    @GetMapping("/{id}")
    public IngredientResponse getIngredientById(@PathVariable Long id) {
        Ingredient i = service.getIngredientById(id);
        return new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags());
    }

    @PutMapping("/{id}")
    public IngredientResponse updateIngredient(@PathVariable Long id, @RequestBody CreateIngredientRequest request) {
        Ingredient i = service.updateIngredient(id, request.getName(), request.getUnit(), request.getTags());
        return new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags());
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Long id) {
        service.deleteIngredient(id);
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return service.getTags();
    }

}

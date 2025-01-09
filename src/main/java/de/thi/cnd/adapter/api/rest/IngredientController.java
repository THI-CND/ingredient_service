package de.thi.cnd.adapter.api.rest;

import de.thi.cnd.adapter.api.rest.dto.CreateIngredientRequest;
import de.thi.cnd.adapter.api.rest.dto.IngredientResponse;
import de.thi.cnd.adapter.api.rest.dto.UpdateIngredientRequest;
import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService service;

    @PostMapping
    public IngredientResponse createIngredient(@RequestBody CreateIngredientRequest request) {
        if(service.getIngredientByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient with name " + request.getName() + " already exists");
        }
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
        Optional<Ingredient> i = service.getIngredientById(id);
        if (i.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found");
        }
        return new IngredientResponse(i.get().getId(), i.get().getName(), i.get().getUnit(), i.get().getTags());
    }

    @PutMapping("/{id}")
    public IngredientResponse updateIngredient(@PathVariable Long id, @RequestBody UpdateIngredientRequest request) {
        Optional<Ingredient> i = service.updateIngredient(id, request.getName(), request.getUnit(), request.getTags());
        if (i.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found");
        }
        return new IngredientResponse(i.get().getId(), i.get().getName(), i.get().getUnit(), i.get().getTags());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        Optional<Ingredient> i = service.getIngredientById(id);
        if (i.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found");
        }
        service.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return service.getTags();
    }

}

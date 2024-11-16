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
    public IngredientResponse createPost(@RequestBody CreateIngredientRequest request) {
        Ingredient i = service.createIngredient(request.getName(), request.getUnit(), request.getTags());
        return new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags());
    }

    @GetMapping
    public List<IngredientResponse> listIngredients() {
        List<Ingredient> list = service.listIngredients();

        List<IngredientResponse> ingredients = new ArrayList<>();

        for(Ingredient i : list) {
            ingredients.add(new IngredientResponse(i.getId(), i.getName(), i.getUnit(), i.getTags()));
        }
        return ingredients;
    }

}

package de.thi.cnd.adapter.jpa;

import de.thi.cnd.adapter.jpa.entities.IngredientEntity;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientJpaAdapter implements IngredientOutputPort {

    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isPresent()) {
            throw new IllegalArgumentException("Ingredient with name " + ingredient.getName() + " already exists");
        }

        IngredientEntity i = new IngredientEntity();
        i.setName(ingredient.getName());
        i.setUnit(ingredient.getUnit());
        i.setTags(formatTags(ingredient.getTags()));

        ingredientRepository.save(i);

        return i.toIngredient();
    }

    @Override
    public List<Ingredient> getIngredients() {
        Iterable<IngredientEntity> all = ingredientRepository.findAll();
        List<Ingredient> ingredients = new ArrayList<>();
        all.forEach(el -> ingredients.add(el.toIngredient()));
        return ingredients;
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findById(id);
        if (ingredientEntity.isEmpty()) {
            throw new IllegalArgumentException("Ingredient with id " + id + " not found");
        }
        return ingredientEntity.get().toIngredient();
    }

    @Override
    public Ingredient updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findById(ingredientId);
        if (ingredientEntity.isEmpty()) {
            throw new IllegalArgumentException("Ingredient with id " + ingredientId + " not found");
        }
        IngredientEntity i = ingredientEntity.get();
        i.setUnit(unit);
        i.setName(name);
        i.setTags(formatTags(tags));

        ingredientRepository.save(i);
        return i.toIngredient();
    }

    @Override
    public void deleteIngredient(Long id) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findById(id);
        if (ingredientEntity.isEmpty()) {
            throw new IllegalArgumentException("Ingredient with id " + id + " not found");
        }
        ingredientRepository.deleteById(id);
    }

    @Override
    public List<String> getTags() {
        return ingredientRepository.findAllTags();
    }

    @Override
    public List<Ingredient> getIngredientsByTag(String tag) {
        Iterable<IngredientEntity> all = ingredientRepository.findByTag(tag);
        List<Ingredient> ingredients = new ArrayList<>();
        all.forEach(el -> ingredients.add(el.toIngredient()));
        return ingredients;
    }

    private List<String> formatTags(List<String> tags) {
        return tags != null ? new ArrayList<>(tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())) : new ArrayList<>();
    }


}

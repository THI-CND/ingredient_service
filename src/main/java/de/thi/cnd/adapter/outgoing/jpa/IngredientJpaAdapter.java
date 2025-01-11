package de.thi.cnd.adapter.outgoing.jpa;

import de.thi.cnd.adapter.outgoing.jpa.entities.IngredientEntity;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientJpaAdapter implements IngredientOutputPort {

    private final IngredientRepository ingredientRepository;

    public IngredientJpaAdapter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

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
    public Optional<Ingredient> getIngredientById(Long id) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findById(id);
        return ingredientEntity.map(IngredientEntity::toIngredient);
    }

    @Override
    public Optional<Ingredient> getIngredientByName(String name) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findByName(name);
        return ingredientEntity.map(IngredientEntity::toIngredient);
    }

    @Override
    public Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        Optional<IngredientEntity> ingredientEntity = ingredientRepository.findById(ingredientId);
        if (ingredientEntity.isEmpty()) {
            return Optional.empty();
        }
        IngredientEntity i = ingredientEntity.get();
        i.setName(name);
        i.setUnit(unit);
        i.setTags(formatTags(tags));
        ingredientRepository.save(i);
        return Optional.of(i.toIngredient());
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
                .toList()) : new ArrayList<>();
    }


}

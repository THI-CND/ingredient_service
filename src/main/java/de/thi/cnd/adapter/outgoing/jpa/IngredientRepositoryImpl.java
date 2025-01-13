package de.thi.cnd.adapter.outgoing.jpa;

import de.thi.cnd.adapter.outgoing.jpa.entities.IngredientEntity;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientRepositoryImpl implements IngredientRepository {

    private final JpaIngredientRepository jpaIngredientRepository;

    public IngredientRepositoryImpl(JpaIngredientRepository jpaIngredientRepository) {
        this.jpaIngredientRepository = jpaIngredientRepository;
    }

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        if (jpaIngredientRepository.findByName(ingredient.getName()).isPresent()) {
            throw new IllegalArgumentException("Ingredient with name " + ingredient.getName() + " already exists");
        }

        IngredientEntity i = new IngredientEntity();
        i.setName(ingredient.getName());
        i.setUnit(ingredient.getUnit());
        i.setTags(formatTags(ingredient.getTags()));

        jpaIngredientRepository.save(i);

        return i.toIngredient();
    }

    @Override
    public List<Ingredient> getIngredients() {
        List<IngredientEntity> all = jpaIngredientRepository.findAll();
        List<Ingredient> ingredients = new ArrayList<>();
        all.forEach(el -> ingredients.add(el.toIngredient()));
        return ingredients;
    }

    @Override
    public Optional<Ingredient> getIngredientById(Long id) {
        Optional<IngredientEntity> ingredientEntity = jpaIngredientRepository.findById(id);
        return ingredientEntity.map(IngredientEntity::toIngredient);
    }

    @Override
    public Optional<Ingredient> getIngredientByName(String name) {
        Optional<IngredientEntity> ingredientEntity = jpaIngredientRepository.findByName(name);
        return ingredientEntity.map(IngredientEntity::toIngredient);
    }

    @Override
    public Optional<Ingredient> updateIngredient(Long ingredientId, String name, String unit, List<String> tags) {
        Optional<IngredientEntity> ingredientEntity = jpaIngredientRepository.findById(ingredientId);
        if (ingredientEntity.isEmpty()) {
            return Optional.empty();
        }
        IngredientEntity i = ingredientEntity.get();
        i.setName(name);
        i.setUnit(unit);
        i.setTags(formatTags(tags));
        jpaIngredientRepository.save(i);
        return Optional.of(i.toIngredient());
    }

    @Override
    public void deleteIngredient(Long id) {
        Optional<IngredientEntity> ingredientEntity = jpaIngredientRepository.findById(id);
        if (ingredientEntity.isEmpty()) {
            throw new IllegalArgumentException("Ingredient with id " + id + " not found");
        }
        jpaIngredientRepository.deleteById(id);
    }

    @Override
    public List<String> getTags() {
        return jpaIngredientRepository.findAllTags();
    }

    @Override
    public List<Ingredient> getIngredientsByTag(String tag) {
        Iterable<IngredientEntity> all = jpaIngredientRepository.findByTag(tag);
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

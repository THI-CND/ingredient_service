package de.thi.cnd.adapter.jpa;

import de.thi.cnd.adapter.jpa.entities.IngredientEntity;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ports.outgoing.IngredientOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientJpaAdapter implements IngredientOutputPort {

    @Autowired
    IngredientRepository repo;

    @Override
    public void save(Ingredient ingredient) {
        IngredientEntity i = new IngredientEntity();
        i.setId(ingredient.getId());
        i.setUnit(ingredient.getUnit());
        i.setName(ingredient.getName());
        i.setTags(ingredient.getTags());

        repo.save(i);
    }

    @Override
    public List<Ingredient> listAll() {
        Iterable<IngredientEntity> all = repo.findAll();
        List<Ingredient> ingredients = new ArrayList<>();
        all.forEach(el -> ingredients.add(el.toIngredient()));
        return ingredients;
    }

    @Override
    public Ingredient findById(Long id) {
        Optional<IngredientEntity> byId = repo.findById(id);
        if(byId.isPresent()) {
            return byId.get().toIngredient();
        }
        return null;
    }

    @Override
    public long count() {
        return this.repo.count();
    }
}

package de.thi.cnd.ports.outgoing;

import de.thi.cnd.domain.model.Ingredient;

import java.util.List;

public interface IngredientOutputPort {

    void save(Ingredient ingredient);

    List<Ingredient> listAll();

    Ingredient findById(Long id);

    long count();

}

package de.thi.cnd.adapter.jpa;

import de.thi.cnd.adapter.jpa.entities.IngredientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {
    Optional<IngredientEntity> findByName(String name);
}

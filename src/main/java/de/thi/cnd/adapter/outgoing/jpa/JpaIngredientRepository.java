package de.thi.cnd.adapter.outgoing.jpa;

import de.thi.cnd.adapter.outgoing.jpa.entities.IngredientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaIngredientRepository extends CrudRepository<IngredientEntity, Long> {

    List<IngredientEntity> findAll();

    Optional<IngredientEntity> findByName(String name);

    @Query("SELECT DISTINCT t FROM IngredientEntity i JOIN i.tags t")
    List<String> findAllTags();

    @Query("SELECT i FROM IngredientEntity i JOIN i.tags t WHERE t = :tag")
    List<IngredientEntity> findByTag(String tag);

}

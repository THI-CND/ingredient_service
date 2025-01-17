package de.thi.cnd.adapter.outgoing.jpa.entities;

import de.thi.cnd.domain.model.Ingredient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String unit;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;

    public IngredientEntity() {
    }

    public Ingredient toIngredient() {
        return new Ingredient(this.getId(), this.getName(), this.getUnit(), this.getTags());
    }

}

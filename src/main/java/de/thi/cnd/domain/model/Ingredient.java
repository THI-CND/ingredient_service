package de.thi.cnd.domain.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class Ingredient {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String unit;
    private List<String> tags;

    public Ingredient() {
        //this.id = null;
    }

}

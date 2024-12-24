package de.thi.cnd.domain.model;

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
    }

}

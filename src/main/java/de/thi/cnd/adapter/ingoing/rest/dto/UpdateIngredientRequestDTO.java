package de.thi.cnd.adapter.ingoing.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateIngredientRequestDTO {

    private String name;
    private String unit;
    private List<String> tags;

}

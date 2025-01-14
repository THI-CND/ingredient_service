package de.thi.cnd.adapter.ingoing.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDTO {

    private Long id;
    private String name;
    private String unit;
    private List<String> tags;

}

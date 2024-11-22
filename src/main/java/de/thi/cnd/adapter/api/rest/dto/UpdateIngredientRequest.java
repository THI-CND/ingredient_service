package de.thi.cnd.adapter.api.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateIngredientRequest {

    private String name;
    private String unit;
    private List<String> tags;

}

package de.thi.cnd.adapter.outgoing.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredientCreatedEvent {

    private Long id;
    private String name;
    private String unit;
    private List<String> tags;

}

package de.thi.cnd.adapter.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagCreatedEvent {

    private String name;

}

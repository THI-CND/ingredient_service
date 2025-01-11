package de.thi.cnd.adapter.outgoing.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagCreatedEvent {

    private String name;

}

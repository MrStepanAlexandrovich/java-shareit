package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private Integer owner;
    private Integer request;

    public Item(
            String name,
            String description,
            Boolean available,
            Integer itemRequest
    ) {
        this.name = name;
        this.description = description;
        this.isAvailable = available;
        this.request = itemRequest;
    }
}

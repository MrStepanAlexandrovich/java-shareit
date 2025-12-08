package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean isAvailable;
    private User owner;
    private ItemRequest request;

    public Item(
            String name,
            String description,
            boolean available,
            ItemRequest itemRequest
    ) {
        this.name = name;
        this.description = description;
        this.isAvailable = available;
        this.request = itemRequest;
    }
}

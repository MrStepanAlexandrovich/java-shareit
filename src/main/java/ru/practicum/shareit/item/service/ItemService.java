package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item get(int id);

    Collection<Item> search(String description);

    Collection<Item> getAll();

    Item edit(int id, Item item);

    Item add(Item item);
}

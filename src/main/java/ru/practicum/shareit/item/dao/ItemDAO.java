package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemDAO {
    Item getById(int id);

    Collection<Item> searchByDesc(String description);

    Collection<Item> getAll();

    Item edit(int id, Item item);

    Item add(Item item);
}

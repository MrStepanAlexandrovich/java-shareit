package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item get(int id);

    Collection<Item> search(String description);

    Item edit(int itemId, Item item, int userId);

    Collection<Item> getAll(int id);

    Item add(Item item);

    Comment addComment(Comment comment);
}

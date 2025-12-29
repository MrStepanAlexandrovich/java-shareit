package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    ItemWithBookingsDto get(int id);

    Collection<Item> search(String description);

    Item edit(int itemId, Item item, int userId);

    Collection<Item> getAll(int id);

    Item add(Item item);

    Comment addComment(Comment comment);
}

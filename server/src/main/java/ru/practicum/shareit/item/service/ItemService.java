package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.Collection;

public interface ItemService {
    ItemWithBookingsDto get(int id);

    Collection<ItemDto> search(String description);

    ItemDto edit(int itemId, ItemDto itemDto, int userId);

    Collection<ItemDto> getAll(int id);

    ItemDto add(ItemCreateDto itemDto, int userId);

    CommentDto addComment(CommentDto commentDto, int userId, int itemId);
}

package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(
            @RequestBody ItemDto item,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(item)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return ItemMapper.toItemDto(itemService.edit(itemId, ItemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return ItemMapper.toItemDto(itemService.get(itemId));
    }

    @GetMapping
    public Collection<ItemDto> getItems() {
        return itemService.getAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemService.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}

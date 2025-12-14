package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity add(
            @RequestBody @Validated ItemDto item,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        item.setOwner(userId);
        return new ResponseEntity(
                ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(item))),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity edit(
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity(ItemMapper.toItemDto(
                itemService.edit(
                        itemId,
                        ItemMapper.toItem(itemDto),
                        userId
                )),
                HttpStatus.OK
        );
    }

    @GetMapping("/{itemId}")
    public ResponseEntity getItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity(
                ItemMapper.toItemDto(itemService.get(itemId)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity(
                itemService.getAll(userId)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity search(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity(
                itemService.search(text)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .toList(),
                HttpStatus.OK
        );
    }
}

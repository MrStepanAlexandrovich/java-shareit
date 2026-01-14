package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> add(
            @RequestBody @Valid ItemCreateDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemService.add(itemDto, userId),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> edit(
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemService.edit(itemId, itemDto, userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemWithBookingsDto> getItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemService.get(itemId),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(
                itemService.getAll(userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> search(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemService.search(text),
                HttpStatus.OK
        );
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid CommentDto commentDto
    ) {
        return new ResponseEntity<>(
                itemService.addComment(commentDto, userId, itemId),
                HttpStatus.OK
        );
    }
}

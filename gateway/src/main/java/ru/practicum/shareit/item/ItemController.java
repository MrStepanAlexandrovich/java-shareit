package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(
            @RequestBody @Valid ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemClient.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> edit(
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemClient.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid CommentDto commentDto
    ) {
        return itemClient.addComment(itemId, userId, commentDto);
    }
}

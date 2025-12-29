package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> add(
            @RequestBody @Valid ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(new User());
        item.getOwner().setId(userId);
        return new ResponseEntity<>(
                ItemMapper.toItemDto(itemService.add(item)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> edit(
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(new User());
        item.getOwner().setId(userId);
        return new ResponseEntity<>(ItemMapper.toItemDto(
                itemService.edit(
                        itemId,
                        item,
                        userId
                )),
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
    public ResponseEntity<List<ItemWithBookingsDto>> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(
                itemService.getAll(userId)
                        .stream()
                        .map(ItemMapper::toItemWithBookingsDto)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(
            @RequestParam("text") String text,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemService.search(text)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .toList(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody Comment comment
    ) {
        comment.setAuthor(new User());
        comment.getAuthor().setId(userId);
        comment.setItem(new Item());
        comment.getItem().setId(itemId);
        comment.setCreated(LocalDateTime.now());

        return new ResponseEntity<>(
                CommentMapper.toCommentDto(itemService.addComment(comment)),
                HttpStatus.OK
        );
    }
}

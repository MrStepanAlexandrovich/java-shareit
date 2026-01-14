package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping
    public ResponseEntity<Collection<ItemRequestResponseDto>> getUsersRequests(
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemRequestService.getUsersRequests(userId),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ItemRequestResponseDto> addRequest(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody ItemRequestCreateDto itemRequestDto
    ) {
        return new ResponseEntity<>(
                itemRequestService.addRequest(userId, itemRequestDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ItemRequestResponseDto>> getAllOtherRequests(
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                itemRequestService.getOthersRequests(userId),
                HttpStatus.OK
        );
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestResponseDto> getRequest(@PathVariable int requestId) {
        return new ResponseEntity<>(
                itemRequestService.getRequest(requestId),
                HttpStatus.OK
        );
    }
}

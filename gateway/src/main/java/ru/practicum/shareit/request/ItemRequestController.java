package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getUsersRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive int userId
    ) {
        return itemRequestClient.getUsersRequests(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @RequestHeader("X-Sharer-User-Id") @Positive int userId,
            @RequestBody @Valid ItemRequestCreateDto itemRequestDto
    ) {
        return itemRequestClient.addRequest(userId, itemRequestDto);

    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive int userId
    ) {
        return itemRequestClient.getOthersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable @Positive int requestId) {
        return itemRequestClient.getRequest(requestId);
    }
}

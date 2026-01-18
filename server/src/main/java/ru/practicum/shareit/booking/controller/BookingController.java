package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> bookItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        bookingCreateDto.setUserId(userId);

        return new ResponseEntity<>(
                bookingService.createBooking(bookingCreateDto),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> bookingApprove(
            @PathVariable int bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                bookingService.approveBooking(userId, bookingId, approved),
                HttpStatus.OK
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable int bookingId) {
        return new ResponseEntity<>(
                bookingService.getBooking(bookingId),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> getBookingsOfUser(
            @RequestParam(value = "state", defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                bookingService.getBookingsOfUser(userId, state),
                HttpStatus.OK
        );
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> getUsersItemsThatBooked(
            @RequestParam(value = "state", defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                bookingService.getBookingsByItemsOwner(userId, state),
                HttpStatus.OK
        );
    }
}

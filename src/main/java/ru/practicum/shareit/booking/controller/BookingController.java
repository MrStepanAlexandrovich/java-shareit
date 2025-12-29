package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> bookItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody BookingCreateDto bookingCreateDto) {
        bookingCreateDto.setUserId(userId);

        return new ResponseEntity<>(
                bookingService.createBooking(BookingMapper.toBooking(bookingCreateDto)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Booking> bookingResponse(
            @PathVariable int bookingId,
            @RequestParam("approved") boolean isApproved,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(new User());
        booking.getBooker().setId(userId);

        if (isApproved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }

        return new ResponseEntity<>(
                bookingService.response(booking),
                HttpStatus.OK
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable int bookingId) {
        return new ResponseEntity<>(
                bookingService.getBooking(bookingId),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<Collection<Booking>> getBookingsOfUser(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                bookingService.getBookingsOfUser(userId, status),
                HttpStatus.OK
        );
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<Item>> getUsersItemsThatBooked(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return new ResponseEntity<>(
                bookingService.getUsersItemsThatBooked(userId, status),
                HttpStatus.OK
        );
    }
}

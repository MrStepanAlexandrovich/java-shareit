package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
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
    public Booking bookItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody BookingCreateDto bookingDto) {
        bookingDto.setUserId(userId);

        return bookingService.createBooking(bookingDto.toBooking());
    }

    @PatchMapping("/{bookingId}")
    public Booking bookingResponse(
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

        return bookingService.response(booking);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable int bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping
    public Collection<Booking> getBookingsOfUser(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBookingsOfUser(userId, status);
    }

    @GetMapping("/owner")
    public Collection<Item> getUsersItemsThatBooked(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getUsersItemsThatBooked(userId, status);
    }
}

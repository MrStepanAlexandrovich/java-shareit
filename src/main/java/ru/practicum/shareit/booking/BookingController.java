package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

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

    @PatchMapping("/bookings/{bookingId}")
    public void bookingResponse(
            @PathVariable int bookingId,
            @RequestParam("approved") boolean isApproved
    ) {
        Booking booking = new Booking();
        booking.setId(bookingId);

        if (isApproved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }

        bookingService.response(booking);
    }

    @GetMapping("/bookings/{bookingId}")
    public Booking getBooking(@PathVariable int bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping ("/bookings")
    public Collection<Booking> getBookingsOfUser(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBookingsOfUser(userId, status);
    }

    @GetMapping("/bookings/owner")
    public Collection<Booking> getBookedItemsOfUser(
            @RequestParam(value = "state", defaultValue = "ALL") Booking.Status status,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBookedItemsOfUser(userId, status);
    }
}

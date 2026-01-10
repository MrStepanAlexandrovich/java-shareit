package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingCreateDto bookingCreateDto);

    BookingDto approveBooking(int userId, int bookingId, boolean isApproved);

    BookingDto getBooking(int bookingId);

    Collection<BookingDto> getBookingsByItemsOwner(int userId, State state);

    Collection<BookingDto> getBookingsOfUser(int userId, State state);
}

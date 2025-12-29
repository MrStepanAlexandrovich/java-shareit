package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingCreateDto bookingCreateDto) {
        Booking booking = new Booking();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setItem(new Item());
        booking.getItem().setId(bookingCreateDto.getItemId());
        booking.setBooker(new User());
        booking.getBooker().setId(bookingCreateDto.getUserId());

        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(booking.getId());
        bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingDto.setStart(booking.getStart());
        bookingDto.setUser(UserMapper.toUserDto(booking.getBooker()));
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setApproved(booking.getStatus());

        return bookingDto;
    }
}

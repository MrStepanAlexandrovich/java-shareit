package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingCreateDto bookingCreateDto) {
        Booking booking = BookingMapper.toBooking(bookingCreateDto);

        booking.setStatus(Status.WAITING);

        User user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new NotFoundException("User with id = " + booking.getBooker().getId()
                        + " wasn't found"));
        booking.setBooker(user);

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            throw new BadRequestException("End time should be after begin time");
        }

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Item with id = " + booking.getItem().getId()
                        + " wasn't found"));

        if (!item.getIsAvailable()) {
            throw new BadRequestException("Item with id = " + booking.getItem().getId() + " is unavailable");
        }

        booking.setItem(item);

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(int userId, int bookingId, boolean isApproved) {
        Booking booking1 = bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id = " + bookingId + " wasn't found"));

        if (booking1.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("User with id = " + userId
                    + " cannot approveBooking on this request");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " wasn't found"));

        if (isApproved) {
            bookingRepository.updateStatus(bookingId, Status.APPROVED);
        } else {
            bookingRepository.updateStatus(bookingId, Status.REJECTED);
        }

        return BookingMapper.toBookingDto(
                bookingRepository.findBooking(bookingId).orElseThrow(() -> new NotFoundException("Not found"))
        );
    }

    @Override
    public BookingDto getBooking(int bookingId) {
        return BookingMapper.toBookingDto(bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id = " + bookingId + " wasn't found")));
    }

    @Override
    public Collection<ItemDto> getUsersItemsThatBooked(int userId, Status status) {
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");

        Collection<Booking> bookings = switch (status) {
            case ALL -> bookingRepository.findByBookerId(userId, newestFirst);

            case PAST -> bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                    newestFirst);

            case FUTURE -> bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                        newestFirst);

            case APPROVED -> bookingRepository.findByBookerIdAndStatus(userId, Status.APPROVED,
                    newestFirst);

            case REJECTED -> bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED,
                    newestFirst);

            default -> null;
        };

        if (bookings.isEmpty()) {
            throw new NotFoundException("Bookings of items with owner's id = " + userId + " weren't found");
        }

        return bookings
                .stream()
                .map(Booking::getItem)
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(int userId, Status status) {
        Collection<Booking> booking;

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");

        if (status.equals(Status.ALL)) {
            booking = bookingRepository.findByBookerId(userId, newestFirst);
        } else {
            booking =  bookingRepository.findByBookerIdAndStatus(userId, status, newestFirst);
        }

        return booking.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}

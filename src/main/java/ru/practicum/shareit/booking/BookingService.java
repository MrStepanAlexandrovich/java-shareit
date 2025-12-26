package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Booking createBooking(Booking booking) {
        booking.setStatus(Booking.Status.WAITING);

        User user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new NotFoundException("User with id = " + booking.getBooker().getId()
                        + " wasn't found"));
        booking.setBooker(user);

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Booking cannot be created in the past");
        }

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

        return booking;
    }

    public Booking response(Booking booking) {
        Booking booking1 = bookingRepository.findBooking(booking.getId())
                .orElseThrow(() -> new NotFoundException("Booking with id = " + booking.getId() + " wasn't found"));

        if (booking1.getItem().getOwner().getId() != booking.getBooker().getId()) {
            throw new ForbiddenException("User with id = " + booking.getBooker().getId()
                    + " cannot response on this request");
        }

        bookingRepository.updateStatus(booking.getId(), booking.getStatus());

        return bookingRepository.findBooking(booking.getId()).orElseThrow(() -> new NotFoundException("Not found"));
    }

    public Booking getBooking(int bookingId) {
        return bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id = " + bookingId + " wasn't found"));
    }

    public Collection<Item> getUsersItemsThatBooked(
            int userId,
            Booking.Status status) {
        return bookingRepository.getUsersItemsThatBooked(userId, status)
                .stream()
                .map(Booking::getItem)
                .toList();
    }

    public Collection<Booking> getBookingsOfUser(int userId, Booking.Status status) {
        if (status.equals(Booking.Status.ALL)) {
            return bookingRepository.findByBookerId(userId);
        } else {
            return bookingRepository.findByBookerIdAndStatus(userId, status);
        }
    }
}

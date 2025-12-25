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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Booking createBooking(Booking booking) {
        booking.setStatus(Booking.Status.WAITING);

        validateItem(booking.getItem().getId());
        validateUser(booking.getBooker().getId());
        int id = bookingRepository.save(booking).getId();

        return bookingRepository.findBooking(id).get();
    }

    public void response(Booking booking) {
        bookingRepository.updateStatus(booking.getId(), booking.getStatus());
    }

    public Booking getBooking(int bookingId) {
        return bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id = " + bookingId + " wasn't found"));
    }

    public Collection<Booking> getBookedItemsOfUser(
            int userId,
            Booking.Status status) {
        return bookingRepository.findByBookerIdAndStatus(userId, status);
    }

    public Collection<Booking> getBookingsOfUser(int userId, Booking.Status status) {
        return List.of();
    }

    private void validateItem(int id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Item with id = " + id + " wasn't found");
        } else {
            Item item = itemOptional.get();

            if (!item.getIsAvailable()) {
                throw new ForbiddenException("Item with id = " + id + " is already booked");
            }
        }
    }

    private void validateUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("Item with id = " + id + " wasn't found");
        }
    }
}

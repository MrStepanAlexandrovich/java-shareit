package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
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
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingCreateDto bookingCreateDto) {
        log.info("Start of creating booking: " + bookingCreateDto.toString());

        Booking booking = BookingMapper.toBooking(bookingCreateDto);

        log.trace("Booking DTO mapped to booking");

        booking.setStatus(Status.WAITING);

        User user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> {
                    log.error("User with id = " + booking.getBooker().getId()
                            + " wasn't found");

                    return new NotFoundException("User with id = " + booking.getBooker().getId()
                            + " wasn't found");
                });

        log.trace("User with id = " + booking.getBooker().getId() + " was found in database");

        booking.setBooker(user);

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            log.error("End time should be after begin time");

            throw new BadRequestException("End time should be after begin time");
        }

        log.trace("Booking start time and end time validated");

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Item with id = " + booking.getItem().getId()
                        + " wasn't found"));

        log.trace("Item with id = " + booking.getItem().getId() + " was found in database");

        if (!item.getIsAvailable()) {
            log.error("Item with id = " + booking.getItem().getId() + " is unavailable");

            throw new BadRequestException("Item with id = " + booking.getItem().getId() + " is unavailable");
        }

        log.trace("Item is available");

        booking.setItem(item);

        bookingRepository.save(booking);

        log.info("Booking saved to database");

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(int userId, int bookingId, boolean isApproved) {
        log.info(String.format("Start of approving booking. User id: %d, booking id: %d, approved: %b",
                userId, bookingId, isApproved));

        Booking booking1 = bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with id = " + bookingId + " wasn't found");

                    return new NotFoundException("Booking with id = " + bookingId + " wasn't found");
                });

        log.trace("Booking with id = " + bookingId + " was found in database");

        if (booking1.getItem().getOwner().getId() != userId) {
            log.error("User with id = " + userId + " cannot approve booking on this request");

            throw new ForbiddenException("User with id = " + userId
                    + " cannot approveBooking on this request");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id = " + userId + " wasn't found");
                    return new NotFoundException("User with id = " + userId + " wasn't found");
                });

        log.trace("User with id = " + userId + " was found in database");

        if (isApproved) {
            bookingRepository.updateStatus(bookingId, Status.APPROVED);
        } else {
            bookingRepository.updateStatus(bookingId, Status.REJECTED);
        }

        log.trace("Booking status updated");

        BookingDto bookingDto = BookingMapper.toBookingDto(
                bookingRepository.findBooking(bookingId).orElseThrow(() -> new NotFoundException("Not found"))
        );

        log.info("Booking with id = " + bookingId + " got status updated in database. Approved: " + isApproved);

        return bookingDto;
    }

    @Override
    public BookingDto getBooking(int bookingId) {
        log.info("Getting booking with id = " + bookingId + "...");
        BookingDto bookingDto = BookingMapper.toBookingDto(bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking with id = " + bookingId + " wasn't found");
                    return new NotFoundException("Booking with id = " + bookingId + " wasn't found"))
                })
        );

        log.info("Booking with id = " + bookingId + " was found in database");
        return bookingDto;
    }

    @Override
    public Collection<BookingDto> getBookingsByItemsOwner(int userId, State state) {
        log.info("Getting booking by items owner and state...");
        log.debug(String.format("User id: %d. State: %s", userId, state.toString()));

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");

        userRepository.findById(userId).orElseThrow(() ->  {
                    log.error("User with id = " + userId + " wasn't found");

                   return new NotFoundException("User with id = " + userId + " wasn't found");
                }
        );

        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItemOwnerId(userId, newestFirst);

            case PAST -> bookingRepository
                    .findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), newestFirst);

            case FUTURE -> bookingRepository
                    .findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), newestFirst);

            case CURRENT -> bookingRepository
                    .findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                            LocalDateTime.now(), newestFirst);

            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(userId, Status.WAITING, newestFirst);

            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatus(userId, Status.REJECTED, newestFirst);
        };

        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();

        log.info("Got bookings from database");

        return bookingDtos;
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(int userId, State state) {
        log.info("Starting getting bookings of user...");
        log.debug(String.format("User id: %d. State: %s", userId, state.toString()));

        userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("User with id = " + userId + " wasn't found");

                    return new NotFoundException("User with id = " + userId + " wasn't found");
                }
        );

        log.trace("User with id = " + userId + " was found in database");

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");

        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerId(userId, newestFirst);

            case PAST -> bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                    newestFirst);

            case FUTURE -> bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                    newestFirst);

            case REJECTED -> bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED,
                    newestFirst);

            case CURRENT -> bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                    LocalDateTime.now(), newestFirst);

            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING);
        };

        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();

        log.info("Got bookings from database");

        return bookingDtos;
    }
}

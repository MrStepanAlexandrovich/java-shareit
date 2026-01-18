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
        log.info("Start of creating booking. User id: {}, item id: {}",
                bookingCreateDto.getUserId(), bookingCreateDto.getItemId());

        Booking booking = BookingMapper.toBooking(bookingCreateDto);

        booking.setStatus(Status.WAITING);

        User user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> {
                    log.error("User with id = {} wasn't found", bookingCreateDto.getUserId());

                    return new NotFoundException("User with id = " + booking.getBooker().getId()
                            + " wasn't found");
                });

        log.trace("User with id = {} was found", booking.getBooker().getId());

        booking.setBooker(user);

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            log.warn("End time should be after begin time. Start time: {}, end time: {}",
                    booking.getStart(), booking.getEnd());

            throw new BadRequestException("End time should be after begin time");
        }

        log.trace("Booking start time = {} and end time = {}. Validated", booking.getStart(), booking.getEnd());

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> {
                    log.warn("Item with id = {} wasn't found", booking.getItem().getId());

                    return new NotFoundException("Item with id = " + booking.getItem().getId()
                            + " wasn't found");
                });

        log.trace("Item with id = {} was found in database", booking.getItem().getId());

        if (!item.getIsAvailable()) {
            log.warn("Item with id = {} is unavailable", booking.getItem().getId());

            throw new BadRequestException("Item with id = " + booking.getItem().getId() + " is unavailable");
        }

        log.trace("Item with id = {} is available", item.getId());

        booking.setItem(item);

        Booking booking1 = bookingRepository.save(booking);

        log.info("Booking saved to database. ID = {}", booking1.getId());

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(int userId, int bookingId, boolean isApproved) {
        log.info("Start of approving booking. User id = {}, booking id = {}, approved: {}", userId, bookingId, isApproved);

        Booking booking1 = bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id = {} wasn't found", bookingId);

                    return new NotFoundException("Booking with id = " + bookingId + " wasn't found");
                });

        log.trace("Booking with id = {} was found in database", bookingId);

        if (booking1.getItem().getOwner().getId() != userId) {
            log.warn("User with id = {} cannot approve booking on this request", userId);

            throw new ForbiddenException("User with id = " + userId
                    + " cannot approveBooking on this request");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id = {} wasn't found", userId);

                    return new NotFoundException("User with id = " + userId + " wasn't found");
                });

        log.trace("User with id = {} was found in database", userId);

        if (isApproved) {
            bookingRepository.updateStatus(bookingId, Status.APPROVED);
        } else {
            bookingRepository.updateStatus(bookingId, Status.REJECTED);
        }

        log.trace("Booking status updated to {}", isApproved ? Status.APPROVED : Status.REJECTED);

        BookingDto bookingDto = BookingMapper.toBookingDto(
                bookingRepository.findBooking(bookingId).orElseThrow(() -> {
                            log.warn("Booking with id = {} wasn't found after status update", bookingId);

                            return new NotFoundException("Not found");
                        }
                )
        );

        log.info("Booking with id = {} got status updated in database. Approved: {}", bookingId,
                isApproved ? Status.APPROVED : Status.REJECTED);

        return bookingDto;
    }

    @Override
    public BookingDto getBooking(int bookingId) {
        log.info("Getting booking with id = {}...", bookingId);

        BookingDto bookingDto = BookingMapper.toBookingDto(bookingRepository.findBooking(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking with id = {} wasn't found", bookingId);

                    return new NotFoundException("Booking with id = " + bookingId + " wasn't found");
                })
        );

        log.info("Booking with id = " + bookingId + " was found in database");
        return bookingDto;
    }

    @Override
    public Collection<BookingDto> getBookingsByItemsOwner(int userId, State state) {
        log.info("Getting booking by items owner and state. User id: {}. State: {}", userId, state.toString());

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "start");

        userRepository.findById(userId).orElseThrow(() -> {
                    log.warn("User with id = {} wasn't found", userId);

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

        List<BookingDto> bookingDtos = bookings
                .stream()
                .peek(b -> log.debug("Found booking with id = {} for item owned by user with id = {}",
                        b.getId(), userId))
                .map(BookingMapper::toBookingDto)
                .toList();

        log.info("Got {} bookings from database with owner's id = {}", bookingDtos.size(), userId);

        return bookingDtos;
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(int userId, State state) {
        log.info("Starting getting bookings of user. User id: {}. State: {}", userId, state.toString());

        userRepository.findById(userId).orElseThrow(
                () -> {
                    log.warn("User with id = {} wasn't found", userId);

                    return new NotFoundException("User with id = " + userId + " wasn't found");
                }
        );

        log.trace("User with id = {} was found in database", userId);

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
                .peek(b -> log.debug("Found booking with id = {} for user with id = {}", b.getId(), userId))
                .map(BookingMapper::toBookingDto)
                .toList();

        log.info("Got {} bookings from database", bookingDtos.size());

        return bookingDtos;
    }
}

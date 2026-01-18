package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingCreateDto createDto;
    private Item item;
    private User user;

    @BeforeEach
    public void setup() {
        createDto = new BookingCreateDto();
        createDto.setItemId(5);
        createDto.setUserId(2);
        createDto.setStart(LocalDateTime.now().plusDays(1));
        createDto.setEnd(LocalDateTime.now().plusDays(2));

        item = new Item();
        item.setId(5);
        item.setIsAvailable(true);
        user = new User(2, "U", "u@e.com");
    }

    @Test
    public void createBookingSuccess() {
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(itemRepository.findById(5)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingDto dto = bookingService.createBooking(createDto);
        assertEquals(Status.WAITING, dto.getStatus());
    }

    @Test
    public void createBookingInvalidTimes() {
        createDto.setStart(LocalDateTime.now().plusDays(5));
        createDto.setEnd(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> bookingService.createBooking(createDto));
    }

    @Test
    public void createBookingItemUnavailable() {
        item.setIsAvailable(false);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(itemRepository.findById(5)).thenReturn(Optional.of(item));
        assertThrows(BadRequestException.class, () -> bookingService.createBooking(createDto));
    }

    @Test
    public void approveBookingForbidden() {
        Booking booking = new Booking();
        Item other = new Item();
        other.setId(5);
        other.setOwner(new User(99, "O", "o@e.com"));
        booking.setItem(other);
        when(bookingRepository.findBooking(1)).thenReturn(Optional.of(booking));
        assertThrows(ForbiddenException.class, () -> bookingService.approveBooking(2, 1, true));
    }

    @Test
    public void approveBookingSuccessApprove() {
        Booking booking = new Booking();
        Item it = new Item();
        it.setId(5);
        it.setOwner(new User(2, "O", "o@e.com"));
        booking.setItem(it);
        when(bookingRepository.findBooking(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        doNothing().when(bookingRepository).updateStatus(1, Status.APPROVED);
        when(bookingRepository.findBooking(1)).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingService.approveBooking(2, 1, true));
    }

    @Test
    public void approveBookingNotFound() {
        when(bookingRepository.findBooking(9)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(2, 9, true));
    }

    @Test
    public void approveBookingReject() {
        Booking booking = new Booking();
        Item it = new Item();
        it.setId(5);
        it.setOwner(new User(2, "O", "o@e.com"));
        booking.setItem(it);
        when(bookingRepository.findBooking(2)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        doNothing().when(bookingRepository).updateStatus(2, Status.REJECTED);
        when(bookingRepository.findBooking(2)).thenReturn(Optional.of(booking));

        assertDoesNotThrow(() -> bookingService.approveBooking(2, 2, false));
    }

    @Test
    public void getBookingNotFound() {
        when(bookingRepository.findBooking(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(2));
    }

    @Test
    public void getBookingsOfUserAll() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        var res = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.ALL);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getBookingsOfUserPast() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        Booking b = new Booking(); b.setEnd(LocalDateTime.now().minusDays(1));
        when(bookingRepository.findByBookerIdAndEndIsBefore(eq(2), any(), any())).thenReturn(List.of(b));
        var res = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.PAST);
        assertEquals(1, res.size());
    }

    @Test
    public void getBookingsOfUserFuture() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        Booking b = new Booking(); b.setStart(LocalDateTime.now().plusDays(5));
        when(bookingRepository.findByBookerIdAndStartIsAfter(eq(2), any(), any())).thenReturn(List.of(b));
        var res = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.FUTURE);
        assertEquals(1, res.size());
    }

    @Test
    public void getBookingsOfUserCurrent() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        Booking b = new Booking(); b.setStart(LocalDateTime.now().minusDays(1)); b.setEnd(LocalDateTime.now().plusDays(1));
        when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(eq(2), any(), any(), any())).thenReturn(List.of(b));
        var res = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.CURRENT);
        assertEquals(1, res.size());
    }

    @Test
    public void getBookingsOfUserWaitingAndRejected() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        Booking b = new Booking();
        when(bookingRepository.findByBookerIdAndStatus(eq(2), eq(Status.WAITING))).thenReturn(List.of(b));
        var res = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.WAITING);
        assertEquals(1, res.size());

        when(bookingRepository.findByBookerIdAndStatus(eq(2), eq(Status.REJECTED), any())).thenReturn(List.of(b));
        var res2 = bookingService.getBookingsOfUser(2, ru.practicum.shareit.booking.model.State.REJECTED);
        assertEquals(1, res2.size());
    }

    @Test
    public void getBookingsByItemsOwnerAllAndStates() {
        when(userRepository.findById(3)).thenReturn(Optional.of(new User(3, "U", "u@e.com")));
        when(bookingRepository.findByItemOwnerId(eq(3), any())).thenReturn(List.of());
        var res = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.ALL);
        assertTrue(res.isEmpty());

        Booking b = new Booking(); b.setEnd(LocalDateTime.now().minusDays(1));
        when(bookingRepository.findByItemOwnerIdAndEndIsBefore(eq(3), any(), any())).thenReturn(List.of(b));
        var past = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.PAST);
        assertEquals(1, past.size());

        Booking bf = new Booking(); bf.setStart(LocalDateTime.now().plusDays(1));
        when(bookingRepository.findByItemOwnerIdAndStartIsAfter(eq(3), any(), any())).thenReturn(List.of(bf));
        var fut = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.FUTURE);
        assertEquals(1, fut.size());

        Booking bc = new Booking(); bc.setStart(LocalDateTime.now().minusDays(1)); bc.setEnd(LocalDateTime.now().plusDays(1));
        when(bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(eq(3), any(), any(), any())).thenReturn(List.of(bc));
        var cur = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.CURRENT);
        assertEquals(1, cur.size());

        when(bookingRepository.findByItemOwnerIdAndStatus(eq(3), eq(Status.WAITING), any())).thenReturn(List.of(bc));
        var wait = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.WAITING);
        assertEquals(1, wait.size());

        when(bookingRepository.findByItemOwnerIdAndStatus(eq(3), eq(Status.REJECTED), any())).thenReturn(List.of(bc));
        var rej = bookingService.getBookingsByItemsOwner(3, ru.practicum.shareit.booking.model.State.REJECTED);
        assertEquals(1, rej.size());
    }

    @Test
    public void createBookingUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());
        createDto.setUserId(99);
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(createDto));
    }

    @Test
    public void createBookingItemNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(itemRepository.findById(99)).thenReturn(Optional.empty());
        createDto.setItemId(99);
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(createDto));
    }

    @Test
    public void createBookingStartEqualsEnd() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        createDto.setStart(now);
        createDto.setEnd(now);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> bookingService.createBooking(createDto));
    }

    @Test
    public void getBookingSuccess() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findBooking(1)).thenReturn(Optional.of(booking));

        BookingDto dto = bookingService.getBooking(1);
        assertEquals(1, dto.getId());
    }

    @Test
    public void getBookingsOfUserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsOfUser(99, ru.practicum.shareit.booking.model.State.ALL));
    }

    @Test
    public void getBookingsByItemsOwnerNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByItemsOwner(99, ru.practicum.shareit.booking.model.State.ALL));
    }
}

package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item;
    private Sort sortByStartDesc;

    @BeforeEach
    void setUp() {
        sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        entityManager.persist(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        entityManager.persist(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setIsAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);
        entityManager.flush();
    }

    @Test
    void findBooking_whenExists_returnsBooking() {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        entityManager.persist(booking);
        entityManager.flush();

        Optional<Booking> found = bookingRepository.findBooking(booking.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBooker().getName()).isEqualTo("Booker");
    }

    @Test
    void findByBookerId_returnsBookerBookings() {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        entityManager.persist(booking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByBookerId(booker.getId(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByBookerIdAndStatus_returnsFilteredBookings() {
        Booking booking1 = new Booking();
        booking1.setBooker(booker);
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setStatus(Status.WAITING);
        entityManager.persist(booking1);
        entityManager.flush();

        Collection<Booking> waitingBookings = bookingRepository.findByBookerIdAndStatus(
                booker.getId(), Status.WAITING, sortByStartDesc);

        assertThat(waitingBookings).hasSize(1);
        assertThat(waitingBookings.iterator().next().getStatus()).isEqualTo(Status.WAITING);
    }

    @Test
    void findByBookerIdAndEndIsBefore_returnsPastBookings() {
        Booking pastBooking = new Booking();
        pastBooking.setBooker(booker);
        pastBooking.setItem(item);
        pastBooking.setStart(LocalDateTime.now().minusDays(5));
        pastBooking.setEnd(LocalDateTime.now().minusDays(3));
        pastBooking.setStatus(Status.APPROVED);
        entityManager.persist(pastBooking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBefore(
                booker.getId(), LocalDateTime.now(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByBookerIdAndStartIsAfter_returnsFutureBookings() {
        Booking futureBooking = new Booking();
        futureBooking.setBooker(booker);
        futureBooking.setItem(item);
        futureBooking.setStart(LocalDateTime.now().plusDays(5));
        futureBooking.setEnd(LocalDateTime.now().plusDays(7));
        futureBooking.setStatus(Status.WAITING);
        entityManager.persist(futureBooking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                booker.getId(), LocalDateTime.now(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByBookerIdAndStartIsBeforeAndEndIsAfter_returnsCurrentBookings() {
        Booking currentBooking = new Booking();
        currentBooking.setBooker(booker);
        currentBooking.setItem(item);
        currentBooking.setStart(LocalDateTime.now().minusDays(1));
        currentBooking.setEnd(LocalDateTime.now().plusDays(1));
        currentBooking.setStatus(Status.APPROVED);
        entityManager.persist(currentBooking);
        entityManager.flush();

        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                booker.getId(), now, now, sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByItemOwnerId_returnsOwnerBookings() {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
        entityManager.persist(booking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByItemOwnerId(owner.getId(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByItemOwnerIdAndStatus_returnsFilteredBookings() {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.REJECTED);
        entityManager.persist(booking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatus(
                owner.getId(), Status.REJECTED, sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByItemOwnerIdAndEndIsBefore_returnsPastBookings() {
        Booking pastBooking = new Booking();
        pastBooking.setBooker(booker);
        pastBooking.setItem(item);
        pastBooking.setStart(LocalDateTime.now().minusDays(5));
        pastBooking.setEnd(LocalDateTime.now().minusDays(3));
        pastBooking.setStatus(Status.APPROVED);
        entityManager.persist(pastBooking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(
                owner.getId(), LocalDateTime.now(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByItemOwnerIdAndStartIsAfter_returnsFutureBookings() {
        Booking futureBooking = new Booking();
        futureBooking.setBooker(booker);
        futureBooking.setItem(item);
        futureBooking.setStart(LocalDateTime.now().plusDays(5));
        futureBooking.setEnd(LocalDateTime.now().plusDays(7));
        futureBooking.setStatus(Status.WAITING);
        entityManager.persist(futureBooking);
        entityManager.flush();

        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(
                owner.getId(), LocalDateTime.now(), sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }

    @Test
    void findByItemOwnerIdAndStartIsBeforeAndEndIsAfter_returnsCurrentBookings() {
        Booking currentBooking = new Booking();
        currentBooking.setBooker(booker);
        currentBooking.setItem(item);
        currentBooking.setStart(LocalDateTime.now().minusDays(1));
        currentBooking.setEnd(LocalDateTime.now().plusDays(1));
        currentBooking.setStatus(Status.APPROVED);
        entityManager.persist(currentBooking);
        entityManager.flush();

        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                owner.getId(), now, now, sortByStartDesc);

        assertThat(bookings).hasSize(1);
    }
}

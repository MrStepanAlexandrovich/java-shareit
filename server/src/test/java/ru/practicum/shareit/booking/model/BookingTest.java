package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {

    @Test
    void testNoArgsConstructor() {
        Booking booking = new Booking();

        assertThat(booking.getId()).isEqualTo(0);
        assertThat(booking.getStart()).isNull();
        assertThat(booking.getEnd()).isNull();
        assertThat(booking.getItem()).isNull();
        assertThat(booking.getBooker()).isNull();
        assertThat(booking.getStatus()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        User booker = new User(1, "Booker", "booker@test.com");
        Item item = new Item();
        item.setId(1);
        LocalDateTime start = LocalDateTime.of(2024, 1, 20, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 25, 10, 0);

        Booking booking = new Booking(1, start, end, item, booker, Status.APPROVED);

        assertThat(booking.getId()).isEqualTo(1);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void testSettersAndGetters() {
        Booking booking = new Booking();
        User booker = new User(2, "John", "john@test.com");
        Item item = new Item();
        item.setId(5);
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 2, 5, 18, 0);

        booking.setId(10);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);

        assertThat(booking.getId()).isEqualTo(10);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(Status.WAITING);
    }
}

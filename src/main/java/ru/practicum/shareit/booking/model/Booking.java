package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@NoArgsConstructor
@Entity
@Data
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime start;

    @Column(name = "\"end\"")
    private LocalDateTime end;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated
    private Status status;

    public enum Status {
        ALL,
        APPROVED,
        WAITING,
        REJECTED,
        CANCELED
    }
}

package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime created;

    private String text;
}

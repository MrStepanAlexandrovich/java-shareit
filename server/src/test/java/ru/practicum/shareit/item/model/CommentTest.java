package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    void testNoArgsConstructor() {
        Comment comment = new Comment();

        assertThat(comment.getId()).isEqualTo(0);
        assertThat(comment.getAuthor()).isNull();
        assertThat(comment.getItem()).isNull();
        assertThat(comment.getCreated()).isNull();
        assertThat(comment.getText()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        Comment comment = new Comment();
        User author = new User(1, "Author", "author@test.com");
        Item item = new Item();
        item.setId(1);
        LocalDateTime created = LocalDateTime.of(2024, 1, 15, 10, 30);

        comment.setId(5);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(created);
        comment.setText("Great item!");

        assertThat(comment.getId()).isEqualTo(5);
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getCreated()).isEqualTo(created);
        assertThat(comment.getText()).isEqualTo("Great item!");
    }
}

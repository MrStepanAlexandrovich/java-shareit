package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        entityManager.persist(user);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setIsAvailable(true);
        item.setOwner(user);
        entityManager.persist(item);
        entityManager.flush();
    }

    @Test
    void findCommentByItemId_returnsCommentsForItem() {
        Comment comment1 = new Comment();
        comment1.setText("Great item!");
        comment1.setAuthor(user);
        comment1.setItem(item);
        comment1.setCreated(LocalDateTime.now());
        entityManager.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Excellent!");
        comment2.setAuthor(user);
        comment2.setItem(item);
        comment2.setCreated(LocalDateTime.now());
        entityManager.persist(comment2);
        entityManager.flush();

        List<Comment> comments = commentRepository.findCommentByItemId(item.getId());

        assertThat(comments).hasSize(2);
    }

    @Test
    void findCommentByItemId_whenNoComments_returnsEmptyList() {
        List<Comment> comments = commentRepository.findCommentByItemId(999);

        assertThat(comments).isEmpty();
    }

    @Test
    void save_createsNewComment() {
        Comment comment = new Comment();
        comment.setText("New comment");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getText()).isEqualTo("New comment");
    }
}

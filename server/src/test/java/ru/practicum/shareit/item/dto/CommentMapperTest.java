package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toCommentDto() {
        User author = new User(2, "Author", "a@example.com");
        Comment comment = new Comment();
        comment.setId(3);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.of(2020,1,1,0,0));
        comment.setText("ok");

        CommentDto dto = CommentMapper.toCommentDto(comment);
        assertEquals(3, dto.getId());
        assertEquals("Author", dto.getAuthorName());
        assertEquals("ok", dto.getText());
        assertEquals(LocalDateTime.of(2020,1,1,0,0), dto.getCreated());
    }
}


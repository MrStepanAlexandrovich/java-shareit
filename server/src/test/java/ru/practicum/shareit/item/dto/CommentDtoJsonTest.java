package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        CommentDto dto = new CommentDto(1, "John Doe", created, "This is a great item!");

        JsonContent<CommentDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("This is a great item!");
        assertThat(result).hasJsonPathValue("$.created");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"authorName\":\"John Doe\",\"created\":\"2024-01-15T10:30:00\",\"text\":\"This is a great item!\"}";

        CommentDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getAuthorName()).isEqualTo("John Doe");
        assertThat(dto.getText()).isEqualTo("This is a great item!");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
    }
}

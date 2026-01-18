package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentDto comment = new CommentDto(1, "Author", LocalDateTime.of(2024, 1, 15, 10, 30), "Great item!");
        ItemDto dto = new ItemDto(1, "Drill", "Electric drill", true, 2, 3, List.of(comment));

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Drill");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Electric drill");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(3);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Drill\",\"description\":\"Electric drill\",\"available\":true,\"owner\":2,\"requestId\":3}";

        ItemDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getDescription()).isEqualTo("Electric drill");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getOwner()).isEqualTo(2);
        assertThat(dto.getRequestId()).isEqualTo(3);
    }

    @Test
    void testDeserializeWithComments() throws Exception {
        String content = "{\"id\":1,\"name\":\"Drill\",\"description\":\"Electric drill\",\"available\":true," +
                "\"comments\":[{\"id\":1,\"authorName\":\"Author\",\"text\":\"Great!\"}]}";

        ItemDto dto = json.parse(content).getObject();

        assertThat(dto.getComments()).hasSize(1);
        assertThat(dto.getComments().get(0).getText()).isEqualTo("Great!");
    }
}

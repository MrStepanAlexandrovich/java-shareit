package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto requester = new UserDto(1, "John", "john@example.com");
        ItemDto item = new ItemDto(1, "Drill", "Electric drill", true, 2, null, null);

        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(1);
        dto.setRequester(requester);
        dto.setDescription("Need a drill");
        dto.setItems(List.of(item));
        dto.setCreated(LocalDateTime.of(2024, 1, 15, 10, 30));

        JsonContent<ItemRequestResponseDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Need a drill");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("John");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).hasJsonPathValue("$.created");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"requester\":{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\"}," +
                "\"description\":\"Need a drill\",\"items\":[{\"id\":1,\"name\":\"Drill\"}]," +
                "\"created\":\"2024-01-15T10:30:00\"}";

        ItemRequestResponseDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getDescription()).isEqualTo("Need a drill");
        assertThat(dto.getRequester().getId()).isEqualTo(1);
        assertThat(dto.getRequester().getName()).isEqualTo("John");
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 30));
    }
}

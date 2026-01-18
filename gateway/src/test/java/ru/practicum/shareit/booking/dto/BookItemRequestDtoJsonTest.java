package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 20, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 25, 10, 0, 0);
        BookItemRequestDto dto = new BookItemRequestDto(1L, start, end);

        JsonContent<BookItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"itemId\":1,\"start\":\"2024-01-20T10:00:00\",\"end\":\"2024-01-25T10:00:00\"}";

        BookItemRequestDto dto = json.parse(content).getObject();

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 20, 10, 0, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 25, 10, 0, 0));
    }

    @Test
    void testDeserializeWithOnlyItemId() throws Exception {
        String content = "{\"itemId\":5}";

        BookItemRequestDto dto = json.parse(content).getObject();

        assertThat(dto.getItemId()).isEqualTo(5L);
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }
}

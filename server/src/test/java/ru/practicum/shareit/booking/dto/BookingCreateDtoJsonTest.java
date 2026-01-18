package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingCreateDtoJsonTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setUserId(1);
        dto.setItemId(2);
        dto.setStart(LocalDateTime.of(2024, 1, 20, 10, 0));
        dto.setEnd(LocalDateTime.of(2024, 1, 25, 10, 0));

        JsonContent<BookingCreateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"userId\":1,\"itemId\":2,\"start\":\"2024-01-20T10:00:00\",\"end\":\"2024-01-25T10:00:00\"}";

        BookingCreateDto dto = json.parse(content).getObject();

        assertThat(dto.getUserId()).isEqualTo(1);
        assertThat(dto.getItemId()).isEqualTo(2);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 20, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 25, 10, 0));
    }
}

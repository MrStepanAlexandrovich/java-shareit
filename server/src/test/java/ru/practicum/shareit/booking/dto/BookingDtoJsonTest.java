package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto booker = new UserDto(1, "John", "john@example.com");
        ItemDto item = new ItemDto(1, "Drill", "Electric drill", true, 2, null, null);
        LocalDateTime start = LocalDateTime.of(2024, 1, 20, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 25, 10, 0);
        BookingDto dto = new BookingDto(1, booker, item, start, end, Status.APPROVED);

        JsonContent<BookingDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("John");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Drill");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"booker\":{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\"}," +
                "\"item\":{\"id\":1,\"name\":\"Drill\",\"description\":\"Electric drill\",\"available\":true}," +
                "\"start\":\"2024-01-20T10:00:00\",\"end\":\"2024-01-25T10:00:00\",\"status\":\"APPROVED\"}";

        BookingDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getBooker().getId()).isEqualTo(1);
        assertThat(dto.getBooker().getName()).isEqualTo("John");
        assertThat(dto.getItem().getId()).isEqualTo(1);
        assertThat(dto.getItem().getName()).isEqualTo("Drill");
        assertThat(dto.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 20, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 25, 10, 0));
    }
}

package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemWithBookingsDtoJsonTest {

    @Autowired
    private JacksonTester<ItemWithBookingsDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(1);
        dto.setName("Drill");
        dto.setDescription("Electric drill");
        dto.setAvailable(true);
        dto.setOwner(2);
        dto.setLastBooking(LocalDate.of(2024, 1, 10));
        dto.setNextBooking(LocalDate.of(2024, 1, 20));

        JsonContent<ItemWithBookingsDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Drill");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Electric drill");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo("2024-01-10");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo("2024-01-20");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Drill\",\"description\":\"Electric drill\",\"available\":true," +
                "\"lastBooking\":\"2024-01-10\",\"nextBooking\":\"2024-01-20\"}";

        ItemWithBookingsDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Drill");
        assertThat(dto.getLastBooking()).isEqualTo(LocalDate.of(2024, 1, 10));
        assertThat(dto.getNextBooking()).isEqualTo(LocalDate.of(2024, 1, 20));
    }
}

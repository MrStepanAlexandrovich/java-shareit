package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestCreateDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemRequestCreateDto dto = new ItemRequestCreateDto();
        dto.setDescription("Need a drill for weekend project");

        JsonContent<ItemRequestCreateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need a drill for weekend project");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"description\":\"Need a drill for weekend project\"}";

        ItemRequestCreateDto dto = json.parse(content).getObject();

        assertThat(dto.getDescription()).isEqualTo("Need a drill for weekend project");
    }

    @Test
    void testDeserializeEmpty() throws Exception {
        String content = "{}";

        ItemRequestCreateDto dto = json.parse(content).getObject();

        assertThat(dto.getDescription()).isNull();
    }
}

package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserEditDtoJsonTest {

    @Autowired
    private JacksonTester<UserEditDto> json;

    @Test
    void testSerialize() throws Exception {
        UserEditDto dto = new UserEditDto();
        dto.setId(1);
        dto.setName("Updated Name");
        dto.setEmail("updated@example.com");

        JsonContent<UserEditDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated Name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("updated@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}";

        UserEditDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Updated Name");
        assertThat(dto.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void testDeserializePartial() throws Exception {
        String content = "{\"name\":\"Only Name\"}";

        UserEditDto dto = json.parse(content).getObject();

        assertThat(dto.getName()).isEqualTo("Only Name");
        assertThat(dto.getEmail()).isNull();
    }
}

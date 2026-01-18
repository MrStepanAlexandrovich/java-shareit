package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserCreateDtoJsonTest {

    @Autowired
    private JacksonTester<UserCreateDto> json;

    @Test
    void testSerialize() throws Exception {
        UserCreateDto dto = new UserCreateDto("John Doe", "john@example.com");

        JsonContent<UserCreateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        UserCreateDto dto = json.parse(content).getObject();

        assertThat(dto.getName()).isEqualTo("John Doe");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }
}

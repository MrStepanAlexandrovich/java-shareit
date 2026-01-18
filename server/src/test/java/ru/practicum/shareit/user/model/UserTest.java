package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1, "John", "john@example.com");

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(5);
        user.setName("Jane");
        user.setEmail("jane@example.com");

        assertThat(user.getId()).isEqualTo(5);
        assertThat(user.getName()).isEqualTo("Jane");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
    }
}

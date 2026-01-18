package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest {

    @Test
    void testDefaultValues() {
        ItemRequest request = new ItemRequest();

        assertThat(request.getId()).isEqualTo(0);
        assertThat(request.getDescription()).isNull();
        assertThat(request.getRequester()).isNull();
        assertThat(request.getCreated()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        ItemRequest request = new ItemRequest();
        User requester = new User(1, "John", "john@test.com");
        LocalDateTime created = LocalDateTime.of(2024, 1, 15, 10, 30);

        request.setId(5);
        request.setDescription("Need a drill");
        request.setRequester(requester);
        request.setCreated(created);

        assertThat(request.getId()).isEqualTo(5);
        assertThat(request.getDescription()).isEqualTo("Need a drill");
        assertThat(request.getRequester()).isEqualTo(requester);
        assertThat(request.getCreated()).isEqualTo(created);
    }

    @Test
    void testRequesterRelation() {
        ItemRequest request = new ItemRequest();
        User requester = new User(2, "Jane", "jane@test.com");

        request.setRequester(requester);

        assertThat(request.getRequester().getId()).isEqualTo(2);
        assertThat(request.getRequester().getName()).isEqualTo("Jane");
        assertThat(request.getRequester().getEmail()).isEqualTo("jane@test.com");
    }
}

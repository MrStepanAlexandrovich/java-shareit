package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    void testNoArgsConstructor() {
        Item item = new Item();

        assertThat(item.getId()).isEqualTo(0);
        assertThat(item.getName()).isNull();
        assertThat(item.getDescription()).isNull();
        assertThat(item.getIsAvailable()).isNull();
        assertThat(item.getOwner()).isNull();
        assertThat(item.getRequest()).isNull();
        assertThat(item.getComments()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        User owner = new User(1, "Owner", "owner@test.com");
        ItemRequest request = new ItemRequest();
        List<Comment> comments = new ArrayList<>();

        Item item = new Item(1, "Drill", "Electric drill", true, owner, request, comments);

        assertThat(item.getId()).isEqualTo(1);
        assertThat(item.getName()).isEqualTo("Drill");
        assertThat(item.getDescription()).isEqualTo("Electric drill");
        assertThat(item.getIsAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getRequest()).isEqualTo(request);
        assertThat(item.getComments()).isEqualTo(comments);
    }

    @Test
    void testSettersAndGetters() {
        Item item = new Item();
        User owner = new User(2, "John", "john@test.com");

        item.setId(10);
        item.setName("Hammer");
        item.setDescription("Steel hammer");
        item.setIsAvailable(false);
        item.setOwner(owner);

        assertThat(item.getId()).isEqualTo(10);
        assertThat(item.getName()).isEqualTo("Hammer");
        assertThat(item.getDescription()).isEqualTo("Steel hammer");
        assertThat(item.getIsAvailable()).isFalse();
        assertThat(item.getOwner()).isEqualTo(owner);
    }
}

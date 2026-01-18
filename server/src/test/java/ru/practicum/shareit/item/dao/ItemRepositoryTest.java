package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        entityManager.persist(owner);
        entityManager.flush();
    }

    @Test
    void findById_whenItemExists_returnsItem() {
        Item item = new Item();
        item.setName("Drill");
        item.setDescription("Electric drill");
        item.setIsAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);
        entityManager.flush();

        Optional<Item> found = itemRepository.findById(item.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Drill");
    }

    @Test
    void findById_whenItemNotExists_returnsEmpty() {
        Optional<Item> found = itemRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void findByOwnerId_returnsOwnerItems() {
        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Desc1");
        item1.setIsAvailable(true);
        item1.setOwner(owner);
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Desc2");
        item2.setIsAvailable(true);
        item2.setOwner(owner);
        entityManager.persist(item2);
        entityManager.flush();

        List<Item> items = itemRepository.findByOwnerId(owner.getId());

        assertThat(items).hasSize(2);
    }

    @Test
    void findByNameContainingIgnoreCase_findsMatchingItems() {
        Item item = new Item();
        item.setName("Electric Drill");
        item.setDescription("Power tool");
        item.setIsAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);
        entityManager.flush();

        Collection<Item> found = itemRepository
                .findByNameContainingIgnoreCaseAndIsAvailableTrueOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(
                        "drill", "drill");

        assertThat(found).hasSize(1);
    }

    @Test
    void findByDescriptionContainingIgnoreCase_findsMatchingItems() {
        Item item = new Item();
        item.setName("Tool");
        item.setDescription("Electric power drill");
        item.setIsAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);
        entityManager.flush();

        Collection<Item> found = itemRepository
                .findByNameContainingIgnoreCaseAndIsAvailableTrueOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(
                        "drill", "drill");

        assertThat(found).hasSize(1);
    }

    @Test
    void findByRequestId_returnsItemsForRequest() {
        Item item = new Item();
        item.setName("Requested Item");
        item.setDescription("Desc");
        item.setIsAvailable(true);
        item.setOwner(owner);
        entityManager.persist(item);
        entityManager.flush();

        Collection<Item> found = itemRepository.findByRequestId(999);

        assertThat(found).isEmpty();
    }
}

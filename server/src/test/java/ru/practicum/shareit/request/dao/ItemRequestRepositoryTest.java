package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;
    private Sort sortByCreatedDesc;

    @BeforeEach
    void setUp() {
        sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "created");

        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        entityManager.persist(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    void findByRequesterId_returnsUserRequests() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Need a drill");
        request.setRequester(user1);
        request.setCreated(LocalDateTime.now());
        entityManager.persist(request);
        entityManager.flush();

        Collection<ItemRequest> requests = itemRequestRepository.findByRequesterId(user1.getId(), sortByCreatedDesc);

        assertThat(requests).hasSize(1);
        assertThat(requests.iterator().next().getDescription()).isEqualTo("Need a drill");
    }

    @Test
    void findByRequesterIdIsNot_returnsOtherUsersRequests() {
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Request from user1");
        request1.setRequester(user1);
        request1.setCreated(LocalDateTime.now());
        entityManager.persist(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Request from user2");
        request2.setRequester(user2);
        request2.setCreated(LocalDateTime.now());
        entityManager.persist(request2);
        entityManager.flush();

        Collection<ItemRequest> requests = itemRequestRepository.findByRequesterIdIsNot(user1.getId(), sortByCreatedDesc);

        assertThat(requests).hasSize(1);
        assertThat(requests.iterator().next().getDescription()).isEqualTo("Request from user2");
    }

    @Test
    void findById_whenExists_returnsRequest() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Test request");
        request.setRequester(user1);
        request.setCreated(LocalDateTime.now());
        entityManager.persist(request);
        entityManager.flush();

        Optional<ItemRequest> found = itemRequestRepository.findById(request.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Test request");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        Optional<ItemRequest> found = itemRequestRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void save_createsNewRequest() {
        ItemRequest request = new ItemRequest();
        request.setDescription("New request");
        request.setRequester(user1);
        request.setCreated(LocalDateTime.now());

        ItemRequest saved = itemRequestRepository.save(request);

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getDescription()).isEqualTo("New request");
    }

    @Test
    void findAll_returnsAllRequests() {
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Request 1");
        request1.setRequester(user1);
        request1.setCreated(LocalDateTime.now());
        entityManager.persist(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Request 2");
        request2.setRequester(user2);
        request2.setCreated(LocalDateTime.now());
        entityManager.persist(request2);
        entityManager.flush();

        assertThat(itemRequestRepository.findAll()).hasSize(2);
    }
}

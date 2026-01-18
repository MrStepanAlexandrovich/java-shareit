package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById_whenUserExists_returnsUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John");
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findById_whenUserNotExists_returnsEmpty() {
        Optional<User> found = userRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void deleteById_removesUser() {
        User user = new User();
        user.setName("ToDelete");
        user.setEmail("delete@example.com");
        entityManager.persist(user);
        entityManager.flush();

        int userId = user.getId();
        userRepository.deleteById(userId);
        entityManager.flush();

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isEmpty();
    }

    @Test
    void save_createsNewUser() {
        User user = new User();
        user.setName("New User");
        user.setEmail("new@example.com");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getName()).isEqualTo("New User");
    }

    @Test
    void findAll_returnsAllUsers() {
        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        entityManager.persist(user2);
        entityManager.flush();

        assertThat(userRepository.findAll()).hasSize(2);
    }
}

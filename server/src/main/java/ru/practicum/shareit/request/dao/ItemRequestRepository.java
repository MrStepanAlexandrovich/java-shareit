package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    Collection<ItemRequest> findByRequesterIdIsNot(int userId, Sort sort);

    Collection<ItemRequest> findByRequesterId(int userId, Sort sort);
}

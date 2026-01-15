package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest();
        itemRequest.setId(5);
        itemRequest.setDescription("Need");
        itemRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void getRequestSuccess() {
        when(itemRequestRepository.findById(5)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(5)).thenReturn(List.of(new Item()));

        var res = itemRequestService.getRequest(5);
        assertEquals(5, res.getId());
    }

    @Test
    void getRequestNotFound() {
        when(itemRequestRepository.findById(6)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequest(6));
    }

    @Test
    void getUsersRequests() {
        when(userRepository.findById(2)).thenReturn(Optional.of(new User(2, "U", "u@e.com")));
        when(itemRequestRepository.findByRequesterId(eq(2), any())).thenReturn(List.of(itemRequest));

        var res = itemRequestService.getUsersRequests(2);
        assertEquals(1, res.size());
    }

    @Test
    void getOthersRequests() {
        when(userRepository.findById(3)).thenReturn(Optional.of(new User(3, "U", "u@e.com")));
        when(itemRequestRepository.findByRequesterIdIsNot(eq(3), any())).thenReturn(List.of(itemRequest));

        var res = itemRequestService.getOthersRequests(3);
        assertEquals(1, res.size());
    }

    @Test
    void addRequestSuccess() {
        when(userRepository.findById(4)).thenReturn(Optional.of(new User(4, "U", "u@e.com")));
        when(itemRequestRepository.save(any())).thenAnswer(i -> {
            ItemRequest r = i.getArgument(0);
            r.setId(10);
            return r;
        });

        ItemRequestCreateDto create = new ItemRequestCreateDto();
        create.setDescription("Need");

        var res = itemRequestService.addRequest(4, create);
        assertEquals(10, res.getId());
    }
}


package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestResponseDto getRequest(int requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("afdsa"));

        ItemRequestResponseDto itemRequestDto = ItemRequestMapper.toItemRequestResponseDto(itemRequest);
        List<ItemDto> itemDtos = itemRepository.findByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                        .toList();

        itemRequestDto.setItems(itemDtos);

        return itemRequestDto;
    }

    @Override
    public Collection<ItemRequestResponseDto> getUsersRequests(int userId) {
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "created");

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = "
                + userId + " wasn't found"));

        return itemRequestRepository.findByRequesterId(userId, newestFirst).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Override
    public Collection<ItemRequestResponseDto> getOthersRequests(int userId) {
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "created");

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = "
                + userId + " wasn't found"));

        return itemRequestRepository.findByRequesterIdIsNot(userId, newestFirst)
                .stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Override
    public ItemRequestResponseDto addRequest(int userId, ItemRequestCreateDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = "
                + userId + " wasn't found"));

        itemRequest.setRequester(requester);

        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestResponseDto(savedItemRequest);
    }
}

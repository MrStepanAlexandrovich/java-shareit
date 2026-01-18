package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestResponseDto getRequest(int requestId) {
        log.info("Getting request with id = {}", requestId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.warn("Request with id = {} wasn't found", requestId);

                    return new NotFoundException("Request with id = " + requestId + " wasn't found");
                });

        log.trace("Request with id = {} was found", requestId);

        ItemRequestResponseDto itemRequestDto = ItemRequestMapper.toItemRequestResponseDto(itemRequest);

        log.trace("Getting items on request with id = {}", requestId);

        List<ItemDto> itemDtos = itemRepository.findByRequestId(requestId)
                .stream()
                .peek(e -> log.debug("Item with id = {} was found", e.getId()))
                .map(ItemMapper::toItemDto)
                .toList();

        itemRequestDto.setItems(itemDtos);

        log.info("Found request with id = {}", requestId);

        return itemRequestDto;
    }

    @Override
    public Collection<ItemRequestResponseDto> getUsersRequests(int userId) {
        log.info("Getting requests of user with id = {}", userId);

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "created");

        userRepository.findById(userId).orElseThrow(() -> {
                    log.warn("User with id = {} wasn't found", userId);

                    return new NotFoundException("User with id = " + userId + " wasn't found");
                }
        );

        List<ItemRequestResponseDto> itemRequestResponseDtos = itemRequestRepository.findByRequesterId(userId, newestFirst)
                .stream()
                .peek(e -> log.debug("Found request with id = {}", e.getId()))
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();

        log.info("Got {} requests of user with id = {}. ", itemRequestResponseDtos.size(), userId);

        return itemRequestResponseDtos;
    }

    @Override
    public Collection<ItemRequestResponseDto> getOthersRequests(int userId) {
        log.info("Getting other user's requests for user with id = {}", userId);

        Sort newestFirst = Sort.by(Sort.Direction.DESC, "created");

        userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id = {} wasn't found", userId);

            return new NotFoundException("User with id = " + userId + " wasn't found");
        });

        List<ItemRequestResponseDto> itemRequestResponseDtos = itemRequestRepository.findByRequesterIdIsNot(userId, newestFirst)
                .stream()
                .peek(e -> log.debug("Found request with id = {}", e.getId()))
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();

        log.info("For user with id = {} found {} other user's requests", userId, itemRequestResponseDtos.size());

        return itemRequestResponseDtos;
    }

    @Override
    public ItemRequestResponseDto addRequest(int userId, ItemRequestCreateDto itemRequestDto) {
        log.info("User with id = {} is adding request...", userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        User requester = userRepository.findById(userId).orElseThrow(() -> {
                    log.warn("User with id = {} wasn't found", userId);

                    return new NotFoundException("User with id = " + userId + " wasn't found");
                }
        );

        itemRequest.setRequester(requester);

        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        log.info("Item request was saved by user with id = {}. Request id = {}", userId, savedItemRequest.getId());

        return ItemRequestMapper.toItemRequestResponseDto(savedItemRequest);
    }
}

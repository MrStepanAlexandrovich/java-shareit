package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestResponseDto getRequest(int requestId);

    Collection<ItemRequestResponseDto> getUsersRequests(int userId);

    Collection<ItemRequestResponseDto> getOthersRequests(int userId);

    ItemRequestResponseDto addRequest(int userId, ItemRequestCreateDto itemRequestDto);
}

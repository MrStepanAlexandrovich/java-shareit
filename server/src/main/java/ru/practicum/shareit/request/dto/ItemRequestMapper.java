package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestCreateDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription(itemRequestDto.getDescription());

        return itemRequest;
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();

        itemRequestResponseDto.setId(itemRequest.getId());

        if (itemRequest.getRequester() != null) {
            itemRequestResponseDto.setRequester(UserMapper.toUserDto(itemRequest.getRequester()));
        }

        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());

        return itemRequestResponseDto;
    }
}

package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                (item.getOwner() != null) ? item.getOwner().getId() : null,
                null,
                item.getComments() != null ? item.getComments()
                        .stream()
                        .map(CommentMapper::toCommentDto)
                        .toList() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null,
                null
        );
    }

    public static ItemWithBookingsDto toItemWithBookingsDto(Item item) {
        ItemWithBookingsDto itemWithBookingsDto = new ItemWithBookingsDto();
        itemWithBookingsDto.setId(item.getId());
        itemWithBookingsDto.setName(item.getName());
        itemWithBookingsDto.setDescription(item.getDescription());
        itemWithBookingsDto.setAvailable(item.getIsAvailable());
        itemWithBookingsDto.setOwner(item.getOwner().getId());
        itemWithBookingsDto.setRequest(null);
        itemWithBookingsDto.setComments(item.getComments()
                .stream()
                .map(CommentMapper::toCommentDto)
                .toList());

        return itemWithBookingsDto;
    }
}

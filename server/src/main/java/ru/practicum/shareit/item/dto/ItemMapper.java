package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setOwner(
                item.getOwner() != null
                        ? item.getOwner().getId()
                        : null
        );

        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
                item.setId(itemDto.getId());
                item.setName(itemDto.getName());
                item.setDescription(itemDto.getDescription());
                item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemWithBookingsDto toItemWithBookingsDto(Item item) {
        ItemWithBookingsDto itemWithBookingsDto = new ItemWithBookingsDto();
        itemWithBookingsDto.setId(item.getId());
        itemWithBookingsDto.setName(item.getName());
        itemWithBookingsDto.setDescription(item.getDescription());
        itemWithBookingsDto.setAvailable(item.getIsAvailable());
        itemWithBookingsDto.setOwner(item.getOwner().getId());
        itemWithBookingsDto.setRequest(null);
        if (item.getComments() != null && !item.getComments().isEmpty()) {
            itemWithBookingsDto.setComments(
                    item.getComments()
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .toList()
            );
        }

        return itemWithBookingsDto;
    }


    public static Item toItem(ItemCreateDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

}

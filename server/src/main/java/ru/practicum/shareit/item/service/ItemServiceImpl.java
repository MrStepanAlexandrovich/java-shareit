package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto edit(
            int itemId,
            ItemDto itemDto,
            int userId
    ) {
        log.info("Start of editing item...");
        log.debug("item id: {}, user id: {},  item dto: {}", itemId, userId, itemDto.toString());

        Item newItem = ItemMapper.toItem(itemDto);
        checkUser(userId, itemId);

        log.trace("User with id = {} validated", userId);

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item;

        if (itemOptional.isEmpty()) {
            log.warn("Item with id = {} doesn't exist", itemId);

            throw new NotFoundException("Item with id = " + itemId + " doesn't exist");
        } else {
            item = itemOptional.get();

            if (newItem.getName() != null) {
                item.setName(newItem.getName());
                log.trace("Item with id = {} set name: {}", itemId, newItem.getName());
            }
            if (newItem.getDescription() != null) {
                item.setDescription(newItem.getDescription());
                log.trace("Item with id = {} set description: {}", itemId, newItem.getDescription());
            }
            if (newItem.getIsAvailable() != null) {
                item.setIsAvailable(newItem.getIsAvailable());
                log.trace("Item with id = {} set available: {}", itemId, newItem.getIsAvailable());
            }
        }

        Item item1 = itemRepository.save(item);

        log.trace("Item with id = {} was succesfully edited", itemId);

        ItemDto itemDto1 = ItemMapper.toItemDto(item1);

        log.trace("Item with id = {} was mapped to DTO", itemId);

        return itemDto1;
    }

    @Override
    public ItemWithBookingsDto get(int id) {
        log.info("Getting item with id = {}", id);

        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            log.info("item with id = {} was found", id);

            return ItemMapper.toItemWithBookingsDto(itemOptional.get());
        } else {
            log.warn("item with id = {} wasn't found", id);

            throw new NotFoundException("item with id = " + id + " wasn't found");
        }
    }

    @Override
    public Collection<ItemDto> search(String description) {
        log.info("Starting searching by description: \"{}\"", description);

        if (description.isBlank()) {
            log.warn("Description is blank");
            return List.of();
        } else {
            Collection<Item> items = itemRepository
                    .findByNameContainingIgnoreCaseAndIsAvailableTrueOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(description, description);

            if (items.isEmpty()) {
                log.info("No items matching description: \"{}\"", description);

                return List.of();
            } else {
                log.info("Found items matching description:\"{}\"", description);

                return items
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .peek(e -> log.debug("Found item with id = {}", e.getId()))
                        .toList();
            }
        }
    }

    @Override
    public Collection<ItemDto> getAll(int id) {
        log.info("Getting all items of user with id = {}", id);
        List<ItemDto> itemsDto = itemRepository.findByOwnerId(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .peek(e -> log.debug("Got item with id = {}", e.getId()))
                .toList();

        return itemsDto;
    }

    @Override
    public ItemDto add(ItemCreateDto itemDto, int userId) {
        log.info("Start creating item...");
        log.debug("Name = {}, user id = {}, request id = {}", itemDto.getName(), userId, itemDto.getRequestId());

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(new User());
        item.getOwner().setId(userId);
        validate(item);

        log.trace("Item name = \"{}\" validated", itemDto.getName());

        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> {
                        log.warn("Request with id = {} doesn't exist", itemDto.getRequestId());

                        return new NotFoundException("Request with id = " + itemDto.getRequestId() + "doesn't exist");
                    });

            log.trace("Request with id = {} was found", itemDto.getRequestId());
            item.setRequest(itemRequest);
        }

        Item item1 = itemRepository.save(item);

        log.info("Item name = {} was saved. ID = {}", item1.getName(), item1.getId());

        return ItemMapper.toItemDto(item1);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, int userId, int itemId) {
        log.info("Start adding comment. Item id = {}, user id = {}, text = {}", itemId, userId, commentDto.getText());

        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id {} wasn't found", userId);

                    return new NotFoundException("User with id " + userId + " wasn't found");
                });

        Collection<Booking> bookingsOfUser = bookingRepository.findByBookerIdAndStatus(userId, Status.APPROVED);

        Booking booking1 = bookingsOfUser.stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .peek(e -> log.debug("Booking with id = {} was found", itemId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User with id = " + userId
                        + " didn't book item with id " + itemId));

        if (booking1.getEnd().isAfter(comment.getCreated())) {
            log.warn("Users can't add comments before ending of booking. Booking id = {}, end time = {}," +
                    " comment creation time = {}", booking1.getId(), booking1.getEnd(), comment.getCreated());

            throw new BadRequestException("Users can't add comments before ending of booking");
        }

        log.trace("Comment's created time = {}, booking end = {}, booking id = {}. Validated.",
                booking1.getId(), booking1.getEnd(), comment.getCreated()););

        comment.setItem(booking1.getItem());

        comment.setAuthor(user);

        Comment comment1 = commentRepository.save(comment);

        log.info("Comment with text = \"{}\" was saved. ID = {}", comment1.getText(), comment1.getId());

        return CommentMapper.toCommentDto(comment1);
    }

    private void checkUser(int userId, int itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isEmpty()) {
            log.warn("item with id = " + itemId + " wasn't found");
            throw new NotFoundException("item with id = " + itemId + " wasn't found");
        } else {
            log.trace("Item with id = " + itemId + " was found");
            if (itemOptional.get().getOwner().getId() != userId) {
                log.warn("User with id = " + userId + " is not an owner of item with id = " + itemId);
                throw new ForbiddenException("User with id = " + userId + " can't update item");
            }
        }
    }

    private void validate(Item item) {
        if (item.getOwner() == null) {
            throw new NotFoundException("User doesn't exist");
        }
        if ((userRepository.findById(item.getOwner().getId())).isEmpty()) {
            throw new NotFoundException("User doesn't exist");
        }
    }
}

package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto edit(
            int itemId,
            ItemDto itemDto,
            int userId
    ) {
        Item newItem = ItemMapper.toItem(itemDto);
        checkUser(userId, itemId);

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item;

        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Item with id = " + itemId + " doesn't exist");
        } else {
            item = itemOptional.get();

            if (newItem.getName() != null) {
                item.setName(newItem.getName());
            }
            if (newItem.getDescription() != null) {
                item.setDescription(newItem.getDescription());
            }
            if (newItem.getIsAvailable() != null) {
                item.setIsAvailable(newItem.getIsAvailable());
            }
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemWithBookingsDto get(int id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            ItemWithBookingsDto itemDto = ItemMapper.toItemWithBookingsDto(itemOptional.get());

            return itemDto;
        } else {
            throw new NotFoundException("item with id = " + id + " wasn't found");
        }
    }

    @Override
    public Collection<ItemDto> search(String description) {
        if (description.isBlank()) {
            return List.of();
        } else {
            return itemRepository
                    .findByNameContainingIgnoreCaseAndIsAvailableTrueOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(description, description)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .toList();
        }
    }

    @Override
    public Collection<ItemDto> getAll(int id) {
        return itemRepository.findByOwnerId(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto add(ItemDto itemDto, int userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(new User());
        item.getOwner().setId(userId);
        validate(item);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, int userId, int itemId) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " wasn't found"));

        Collection<Booking> bookingsOfUser = bookingRepository.findByBookerIdAndStatus(userId, Status.APPROVED);

        Booking booking1 = bookingsOfUser.stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .findAny()
                .orElseThrow(() -> new NotFoundException("User with id = " + userId
                        + " didn't book item with id " + itemId));

        if (booking1.getEnd().isAfter(comment.getCreated())) {
            throw new BadRequestException("Users can't add comments before ending of booking");
        }

        comment.setItem(booking1.getItem());;
        comment.setAuthor(user);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public List<Comment> getCommentsForItem(int itemId) {
        return commentRepository.findCommentByItemId(itemId);
    }

    private void checkUser(int userId, int itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isEmpty()) {
            throw new NotFoundException("item with id = " + itemId + " wasn't found");
        } else {
            if (itemOptional.get().getOwner().getId() != userId) {
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

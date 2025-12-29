package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
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
    public Item edit(
            int itemId,
            Item newItem,
            int userId
    ) {
        checkUser(userId, itemId);

        Optional<Item> itemOptional = itemRepository.findById((long) itemId);
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

        return itemRepository.save(item);
    }

    @Override
    public Item get(int id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            return itemOptional.get();
        } else {
            throw new NotFoundException("item with id = " + id + " wasn't found");
        }
    }

    @Override
    public Collection<Item> search(String description) {
        if (description.isBlank()) {
            return List.of();
        } else {
            return itemRepository
                    .findByNameContainingIgnoreCaseAndIsAvailableTrueOrDescriptionContainingIgnoreCaseAndIsAvailableTrue(description, description);
        }
    }

    @Override
    public Collection<Item> getAll(int id) {
        return itemRepository.findByOwnerId(id);
    }

    @Override
    public Item add(Item item) {
        validate(item);
        return itemRepository.save(item);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setCreated(LocalDateTime.now());
        User user = userRepository.findById(comment.getAuthor().getId())
                .orElseThrow(() -> new NotFoundException("User with id " + comment.getAuthor().getId()
                        + " wasn't found"));

        comment.setAuthor(user);

        Collection<Booking> bookingsOfUser = bookingRepository.findByBookerIdAndStatus(comment.getAuthor().getId(),
                Booking.Status.APPROVED);

        Booking booking1 = bookingsOfUser.stream()
                .filter(booking -> booking.getItem().getId() == comment.getItem().getId())
                .findAny()
                .orElseThrow(() -> new NotFoundException("User with id = " + comment.getAuthor().getId()
                        + " didn't book item with id " + comment.getItem().getId()));

        comment.setItem(booking1.getItem());

        return commentRepository.save(comment);
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

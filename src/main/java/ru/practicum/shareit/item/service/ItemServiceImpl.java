package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(description, description);
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

    private void checkUser(int userId, int itemId) {
        Optional<Item> itemOptional = itemRepository.findById((long) itemId);

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

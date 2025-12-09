package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ForbiddenException;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDAO;
    private final UserDao userDAO;

    @Override
    public Item edit(
            int itemId,
            Item item,
            int userId
    ) {
        checkUser(userId, itemId);
        return itemDAO.edit(itemId, item);
    }

    @Override
    public Item get(int id) {
        return itemDAO.getById(id);
    }

    @Override
    public Collection<Item> search(String description) {
        return itemDAO.searchByDesc(description);
    }

    @Override
    public Collection<Item> getAll(int id) {
        return itemDAO.getAllOfUser(id);
    }

    @Override
    public Item add(Item item) {
        validate(item);
        return itemDAO.add(item);
    }

    private void checkUser(int userId, int itemId) {
       if (itemDAO.getById(itemId).getOwner() != userId) {
           throw new ForbiddenException("User with id = " + userId + " can't update item");
       }
    }

    private void validate(Item item) {
        if (item.getOwner() == null) {
            throw new NotFoundException("User doesn't exist");
        }
        if (userDAO.get(item.getOwner()) == null) {
            throw new NotFoundException("User doesn't exist");
        }
    }
}

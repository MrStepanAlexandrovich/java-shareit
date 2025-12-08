package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDAO;

    @Override
    public Item edit(int id, Item item) {
        return itemDAO.edit(id, item);
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
    public Collection<Item> getAll() {
        return itemDAO.getAll();
    }

    @Override
    public Item add(Item item) {
        return itemDAO.add(item);
    }
}

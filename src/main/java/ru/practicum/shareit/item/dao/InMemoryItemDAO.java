package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class InMemoryItemDAO implements ItemDAO {
    private static int counter = 0;
    private final List<Item> items = new ArrayList<>();

    @Override
    public Item getById(int id) {
        return items.get(id);
    }

    @Override
    public Collection<Item> searchByDesc(String description) {
        return items.stream()
                .filter(item -> item.getDescription().contains(description))
                .toList();
    }

    @Override
    public Collection<Item> getAll() {
        return items;
    }

    @Override
    public Item edit(int id, Item item) {
        Item oldItem = items.get(id);
        oldItem.setName(item.getName());
        oldItem.setDescription(item.getDescription());
        oldItem.setAvailable(item.isAvailable());
        return oldItem;
    }

    @Override
    public Item add(Item item) {
        items.add(item);
        item.setId(++counter);
        return item;
    }
}

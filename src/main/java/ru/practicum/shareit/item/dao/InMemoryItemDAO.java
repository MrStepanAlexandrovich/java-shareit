package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class InMemoryItemDAO implements ItemDAO {
    private static int counter = 0;
    private final Map<Integer, Item> items = new HashMap();

    @Override
    public Item getById(int id) {
        return items.get(id);
    }

    @Override
    public Collection<Item> searchByDesc(String description) {
        if (!description.isEmpty()) {
            return items.values()
                    .stream()
                    .filter(item -> item.getDescription().toLowerCase().contains(description.toLowerCase())
                            || item.getName().toLowerCase().contains(description.toLowerCase()))
                    .filter(Item::getIsAvailable)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<Item> getAllOfUser(int id) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner() == id)
                .toList();
    }

    @Override
    public Item edit(int id, Item item) {
        Item oldItem = items.get(id);

        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }

        if (item.getIsAvailable() != null) {
            oldItem.setIsAvailable(item.getIsAvailable());
        }

        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }

        return oldItem;
    }

    @Override
    public Item add(Item item) {
        items.put(++counter, item);
        item.setId(counter);
        return item;
    }
}

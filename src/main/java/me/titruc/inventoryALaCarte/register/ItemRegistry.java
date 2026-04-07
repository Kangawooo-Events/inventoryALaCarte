package me.titruc.inventoryALaCarte.register;

import org.bukkit.inventory.ItemStack;
import java.util.HashMap;

public class ItemRegistry {

    private final HashMap<String, ItemStack> data = new HashMap<>();

    public void register(String key, ItemStack item) {
        data.put(key, item.clone());
    }

    public ItemStack get(String key) {
        ItemStack item = data.get(key);
        return item != null ? item.clone() : null;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public void unregister(String key) {
        data.remove(key);
    }
}
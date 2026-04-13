package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent.template.GiveItemStack;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GiveItemFromRegistry extends GiveItemStack {

    @Override
    public ItemStack itemToGive(Player player, MenuHolder menuHolder, ItemStack itemStack, Map<String, Object> map) {

        if (!map.containsKey("key")) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] give_item_from_registry require fields : key");
            return null;
        }

        String key = map.get("key").toString();

        if (!InventoryALaCarte.itemRegistry.has(key)) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] give_item_from_registry : key '" + key + "' not found in registry");
            return null;
        }

        int amount = map.containsKey("amount")
                ? Integer.parseInt(map.get("amount").toString())
                : 1;

        ItemStack result = InventoryALaCarte.itemRegistry.get(key).clone();
        result.setAmount(amount);

        return result;
    }
}
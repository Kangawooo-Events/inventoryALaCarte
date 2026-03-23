package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface ClickableEvent {

    public abstract void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params);

}
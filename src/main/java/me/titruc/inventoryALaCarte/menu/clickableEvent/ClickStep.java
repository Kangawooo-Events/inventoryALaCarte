package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ClickStep {
    void execute(Player player, MenuHolder menu, ItemStack item);
}
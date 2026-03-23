package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CloseMenuEvent implements ClickableEvent {
    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {
        player.closeInventory();
    }
}

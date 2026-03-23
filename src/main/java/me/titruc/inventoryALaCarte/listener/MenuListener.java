package me.titruc.inventoryALaCarte.listener;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(!(event.getInventory().getHolder() instanceof MenuHolder menu)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();

        menu.handleClick(slot, (Player) event.getWhoClicked());
    }
}

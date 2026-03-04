package me.titruc.inventoryALaCarte.inventoryHandler.menu;

import me.titruc.inventoryALaCarte.inventoryHandler.MenuHolder;
import org.bukkit.event.inventory.InventoryType;

public class Generic extends MenuHolder {

    public Generic(int rows)
    {
        super(rows, CHEST_COLUMN_NUMBER, InventoryType.CHEST);
    }
}

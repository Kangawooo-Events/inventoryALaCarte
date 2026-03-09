package me.titruc.inventoryALaCarte.menu.menuUI.layout;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.event.inventory.InventoryType;

public class MenuGeneric extends MenuHolder {

    public MenuGeneric(int rows)
    {
        super(rows, CHEST_COLUMN_NUMBER, InventoryType.CHEST);
    }
}

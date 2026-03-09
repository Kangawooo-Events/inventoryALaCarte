package me.titruc.inventoryALaCarte.menu.menuUI;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MenuHolder implements InventoryHolder {

    public static final int CHEST_COLUMN_NUMBER = 9 ;

    private final Inventory inventory;
    private final int rows;
    private final int columns;


    public MenuHolder(int rows, int columns, InventoryType invType) {
        this.rows = rows;
        this.columns = columns;
        if(invType == InventoryType.CHEST)
        {
            this.inventory = InventoryALaCarte.singleton.getServer().createInventory(null, CHEST_COLUMN_NUMBER * rows);
        }
        else
        {
            this.inventory = InventoryALaCarte.singleton.getServer().createInventory(null, invType);
        }

    }

    public void showInventory(Player player)
    {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}

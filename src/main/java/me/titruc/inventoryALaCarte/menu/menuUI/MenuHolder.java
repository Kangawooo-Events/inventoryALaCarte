package me.titruc.inventoryALaCarte.menu.menuUI;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.clickableEvent.MenuAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuHolder implements InventoryHolder {

    public static final int CHEST_COLUMN_NUMBER = 9;

    private Inventory inventory;
    private final int rows;
    private final int columns;
    private final InventoryType type;
    private Component title = Component.empty();
    private final Map<Integer, List<MenuAction>> actions = new HashMap<>();

    public MenuHolder(int rows, int columns, InventoryType invType) {
        this.rows = rows;
        this.columns = columns;
        this.type = invType;

        createInventory();
    }

    private void createInventory() {
        if (type == InventoryType.CHEST) {
            inventory = InventoryALaCarte.singleton
                    .getServer()
                    .createInventory(this, CHEST_COLUMN_NUMBER * rows, title);
        } else {
            inventory = InventoryALaCarte.singleton
                    .getServer()
                    .createInventory(this, type, title);
        }
    }

    public void setTitle(Component title) {
        this.title = title;
        createInventory();
    }

    public void showInventory(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void handleClick(int slot, Player player) {
        List<MenuAction> actionList = actions.get(slot);

        if (actionList != null) {
            for (MenuAction action : actionList) {
                action.execute(player, this, inventory.getItem(slot));
            }
        }
    }

    public void addMenuAction(int slot, String clickableEventName, Map<String, Object> parameter, Map<String, Map<String, Object>> conditions)
    {
        actions.computeIfAbsent(slot, k -> new ArrayList<>())
                .add(new MenuAction(clickableEventName, parameter, conditions));
    }
}
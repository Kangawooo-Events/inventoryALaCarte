package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class MenuAction {

    private final List<ClickStep> steps;

    public MenuAction(List<ClickStep> steps) {
        this.steps = steps;
    }

    public void execute(Player player, MenuHolder menu, ItemStack item) {
        for (ClickStep step : steps) {
            step.execute(player, menu, item);
        }
    }
}
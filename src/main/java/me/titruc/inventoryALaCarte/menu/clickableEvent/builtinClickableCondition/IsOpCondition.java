package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableCondition;

import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableCondition;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class IsOpCondition implements ClickableCondition {

    @Override
    public boolean test(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {
        return player.isOp();
    }
}
package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableCondition;

import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableCondition;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class IsGameMode implements ClickableCondition {

    @Override
    public boolean test(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {
        if (!params.containsKey("gamemode")) {
            return false;
        }
        try {
            GameMode required = GameMode.valueOf(params.get("gamemode").toString().toUpperCase());
            return player.getGameMode() == required;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
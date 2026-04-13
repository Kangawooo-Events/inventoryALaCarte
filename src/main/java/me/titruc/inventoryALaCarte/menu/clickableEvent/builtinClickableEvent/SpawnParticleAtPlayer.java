package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpawnParticleAtPlayer extends SpawnParticleAtPosition {
    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {
        Location playerLoc = player.getLocation();

        params.put("x", playerLoc.getBlockX());
        params.put("y", playerLoc.getBlockY());
        params.put("z", playerLoc.getBlockZ());

        displayParticle(player, menu,  item, params);
    }
}
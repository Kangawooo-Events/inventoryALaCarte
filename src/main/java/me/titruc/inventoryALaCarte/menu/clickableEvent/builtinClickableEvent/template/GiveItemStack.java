package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent.template;

import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GiveItemStack implements ClickableEvent {
    @Override
    public void execute(Player player, MenuHolder menuHolder, ItemStack itemStack, Map<String, Object> map) {

        give(player, itemToGive(player, menuHolder, itemStack, map));
    }

    //override this one
    public ItemStack itemToGive(Player player, MenuHolder menuHolder, ItemStack itemStack, Map<String, Object> map)
    {
        return new ItemStack(Material.AIR);
    }

    private void give(Player player, ItemStack itemToGive)
    {
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(itemToGive);

        for (ItemStack remaining : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), remaining);
        }
    }
}

package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent.template.GiveItemStack;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GiveItemFromMaterial extends GiveItemStack {
    @Override
    public ItemStack itemToGive(Player player, MenuHolder menuHolder, ItemStack itemStack, Map<String, Object> map) {
        if(!map.containsKey("material"))
        {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] " + "condition require parameter : \"material\"");
        }
        Material itemType = Material.getMaterial((String) map.get("material"));
        ItemStack itemToGive = null;
        if(itemType == null) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] " + "parameter : \"material\" isn't valid");
            return null;
        }
        else {
            itemToGive = new ItemStack(itemType);
        }
        if(!map.containsKey("amount")){
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] " + "condition require parameter : \"amount\"");
            return null;
        }
        else {
            int itemCount = (int) map.get("amount");
            itemToGive.setAmount(itemCount);
        }

        return itemToGive;
    }
}

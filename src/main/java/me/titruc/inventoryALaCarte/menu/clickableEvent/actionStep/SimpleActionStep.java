package me.titruc.inventoryALaCarte.menu.clickableEvent.actionStep;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickStep;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SimpleActionStep implements ClickStep {

    private final String actionName;
    private final Map<String, Object> params;

    public SimpleActionStep(String actionName, Map<String, Object> params) {
        this.actionName = actionName;
        this.params = params;
    }

    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item) {
        if (!InventoryALaCarte.clickableEventRegister.has(actionName))
        {
            InventoryALaCarte.singleton.getLogger().warning("Unknow Action: \"" + actionName + "\" — maybe a typo ?");
            return;
        }

        InventoryALaCarte.clickableEventRegister
                .getFromKey(actionName)
                .execute(player, menu, item, params);
    }
}
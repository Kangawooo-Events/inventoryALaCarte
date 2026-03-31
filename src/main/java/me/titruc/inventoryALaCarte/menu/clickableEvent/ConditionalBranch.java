package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConditionalBranch {

    private final List<ClickableConditionEntry> conditions;
    private final List<ClickStep> actions;

    public ConditionalBranch(List<ClickableConditionEntry> conditions, List<ClickStep> actions) {
        this.conditions = conditions;
        this.actions = actions;
    }

    public boolean test(Player player, MenuHolder menu, ItemStack item) {
        if (conditions.isEmpty()) return true;

        Boolean result = null;

        for (ClickableConditionEntry entry : conditions) {

            if (!InventoryALaCarte.clickableConditionRegister.has(entry.type))
            {
                InventoryALaCarte.singleton.getLogger().warning("Unknow Action : \"" + entry.type + "\" — maybe a typo ?");
                continue;
            }

            boolean test = InventoryALaCarte.clickableConditionRegister
                    .getFromKey(entry.type)
                    .test(player, menu, item, entry.params);


            if (entry.not) test = !test;

            if (result == null) {
                result = test;
            } else {
                switch (entry.logic.toUpperCase()) {
                    case "OR"  -> result = result || test;
                    case "NOR" -> result = !(result || test);
                    default    -> result = result && test; // AND (default)
                }
            }
        }

        return result == null || result;
    }

    public void execute(Player player, MenuHolder menu, ItemStack item) {
        for (ClickStep action : actions) {
            action.execute(player, menu, item);
        }
    }
}
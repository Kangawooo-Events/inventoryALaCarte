package me.titruc.inventoryALaCarte.menu.clickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class MenuAction {

    private final String clickEventName;
    private final Map<String, Object> parameter;
    private final List<ClickableConditionEntry> conditions;


    public MenuAction(String clickEventName, Map<String, Object> parameter, List<ClickableConditionEntry> conditions)
    {
        this.clickEventName = clickEventName;
        this.parameter = parameter;
        this.conditions = conditions;
    }

    public void execute(Player player, MenuHolder menu, ItemStack item) {

        if (!evaluateConditions(player, menu, item)) return;

        if (InventoryALaCarte.clickableEventRegister.has(clickEventName)) {
            InventoryALaCarte.clickableEventRegister
                    .getFromKey(clickEventName)
                    .execute(player, menu, item, parameter);
        }
    }

    public boolean evaluateConditions(Player player, MenuHolder menu, ItemStack item) {
        if (conditions.isEmpty()) return true;

        Boolean result = null;

        for (ClickableConditionEntry entry : conditions) {
            if (!InventoryALaCarte.clickableConditionRegister.has(entry.type)) continue;

            boolean test = InventoryALaCarte
                    .clickableConditionRegister
                    .getFromKey(entry.type)
                    .test(player, menu, item, entry.params);

            if (entry.not) test = !test;

            if (result == null) {
                result = test;
            } else {
                switch (entry.logic.toUpperCase()) {
                    case "AND":
                        result = result && test;
                        break;
                    case "OR":
                        result = result || test;
                        break;
                    case "NOR":
                        result = !(result || test);
                        break;
                    default:
                        result = result && test;
                }
            }
        }
        if (result == null) return true;

        return result;
    }

}

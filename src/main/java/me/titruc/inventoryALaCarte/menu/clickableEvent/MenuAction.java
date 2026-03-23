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
    private final Map<String, Map<String, Object>> conditions;


    public MenuAction(String clickEventName, Map<String, Object> parameter, Map<String, Map<String, Object>> conditions)
        {
            this.clickEventName = clickEventName;
            this.parameter = parameter;
            this.conditions = conditions;
        }
        public void execute(Player player, MenuHolder menu, ItemStack item)
        {
            for (String conditionName : conditions.keySet()) {
                if (InventoryALaCarte.clickableConditionRegister.has(conditionName)) {
                    if(!InventoryALaCarte.clickableConditionRegister.getFromKey(conditionName).test(player, menu, item, conditions.get(conditionName)))
                        return;
                }
            }

            if(InventoryALaCarte.clickableEventRegister.has(clickEventName))
            {
                InventoryALaCarte.clickableEventRegister.getFromKey(clickEventName).execute(player, menu, item, parameter);
            }
        }
}

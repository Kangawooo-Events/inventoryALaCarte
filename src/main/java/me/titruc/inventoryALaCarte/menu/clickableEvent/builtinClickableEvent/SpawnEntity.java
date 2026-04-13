package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpawnEntity implements ClickableEvent {
    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {

        if(!params.containsKey("entity") ||
                !params.containsKey("x") ||
                !params.containsKey("y") ||
                !params.containsKey("z"))
        {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] spawn_entity require fields : entity, x, y, z");
            return;
        }

        try {
            String entityName = params.get("entity").toString().toUpperCase();

            double x = Double.parseDouble(params.get("x").toString());
            double y = Double.parseDouble(params.get("y").toString());
            double z = Double.parseDouble(params.get("z").toString());

            EntityType type = EntityType.valueOf(entityName);

            Location loc = new Location(player.getWorld(), x, y, z);

            player.getWorld().spawnEntity(loc, type);

        } catch (Exception e) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] Error while spawning entity: " + e.getMessage());
        }
    }
}
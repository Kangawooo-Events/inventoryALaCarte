package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;


import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlaySoundAt implements ClickableEvent {
    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {

        if(!params.containsKey("namespace") ||
                !params.containsKey("sound") ||
                !params.containsKey("x") ||
                !params.containsKey("y") ||
                !params.containsKey("z"))
        {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] PlaysoundAt require fields : namespace, sound, x, y, z");
            return;
        }

        try {
            String namespace = params.get("namespace").toString();
            String sound = params.get("sound").toString();

            double x = Double.parseDouble(params.get("x").toString());
            double y = Double.parseDouble(params.get("y").toString());
            double z = Double.parseDouble(params.get("z").toString());

            double radius = params.containsKey("radius")
                    ? Double.parseDouble(params.get("radius").toString())
                    : 16.0;

            float volume = params.containsKey("volume")
                    ? Float.parseFloat(params.get("volume").toString())
                    : 1f;

            float pitch = params.containsKey("pitch")
                    ? Float.parseFloat(params.get("pitch").toString())
                    : 1f;

            String soundKey = params.get("sound").toString();

            Sound soundToPlay = Sound.sound(
                    Key.key(namespace, sound),
                    Sound.Source.MASTER ,
                    volume,
                    pitch
            );

            World world = player.getWorld();

            Location center = new Location(world, x, y, z);

            Collection<Entity> nearby = world.getNearbyEntities(center, radius, radius, radius,
                    e -> e instanceof Player);

            List<Audience> audiences = nearby.stream()
                    .map(e -> (Player) e)
                    .map(p -> (Audience) p)
                    .toList();

            Audience audience = Audience.audience(audiences);
            audience.playSound(soundToPlay);

        } catch (Exception e) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] Error while playing sound : " + e.getMessage());
        }


    }
}
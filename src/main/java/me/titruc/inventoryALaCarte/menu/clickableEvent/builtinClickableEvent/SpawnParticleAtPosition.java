package me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

public class SpawnParticleAtPosition implements ClickableEvent {
    @Override
    public void execute(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params) {
        displayParticle(player, menu, item, params);
    }

    void displayParticle(Player player, MenuHolder menu, ItemStack item, Map<String, Object> params)
    {
        if(!params.containsKey("particle") ||
                !params.containsKey("x") ||
                !params.containsKey("y") ||
                !params.containsKey("z"))
        {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] spawn_particle require fields : particle, x, y, z");
            return;
        }

        try {
            String particleName = params.get("particle").toString().toUpperCase();

            double x = Double.parseDouble(params.get("x").toString());
            double y = Double.parseDouble(params.get("y").toString());
            double z = Double.parseDouble(params.get("z").toString());

            int count = params.containsKey("count")
                    ? Integer.parseInt(params.get("count").toString())
                    : 10;

            double offsetX = params.containsKey("offsetX")
                    ? Double.parseDouble(params.get("offsetX").toString())
                    : 0;

            double offsetY = params.containsKey("offsetY")
                    ? Double.parseDouble(params.get("offsetY").toString())
                    : 0;

            double offsetZ = params.containsKey("offsetZ")
                    ? Double.parseDouble(params.get("offsetZ").toString())
                    : 0;
            Particle particle = Particle.valueOf(particleName);

            Location loc = new Location(player.getWorld(), x, y, z);

            Object data = null;

            if(particle == Particle.ENTITY_EFFECT || particle == Particle.FLASH || particle == Particle.TINTED_LEAVES)
            {
                float r = params.containsKey("r") ? Float.parseFloat(params.get("r").toString()) : 1f;
                float g = params.containsKey("g") ? Float.parseFloat(params.get("g").toString()) : 0f;
                float b = params.containsKey("b") ? Float.parseFloat(params.get("b").toString()) : 0f;
                float a = params.containsKey("a") ? Float.parseFloat(params.get("a").toString()) : 1f;

                data = Color.fromARGB((int)(a * 255), (int)(r * 255), (int)(g * 255), (int)(b * 255));
            }

            else if (particle == Particle.DUST) {
                float r = params.containsKey("r") ? Float.parseFloat(params.get("r").toString()) : 1f;
                float g = params.containsKey("g") ? Float.parseFloat(params.get("g").toString()) : 0f;
                float b = params.containsKey("b") ? Float.parseFloat(params.get("b").toString()) : 0f;
                float size = params.containsKey("size") ? Float.parseFloat(params.get("size").toString()) : 1f;

                data = new Particle.DustOptions(
                        Color.fromRGB((int)(r * 255), (int)(g * 255), (int)(b * 255)),
                        size
                );
            }

            else if(particle == Particle.DUST_COLOR_TRANSITION)
            {
                float fr = params.containsKey("fromR") ? Float.parseFloat(params.get("fromR").toString()) : 1f;
                float fg = params.containsKey("fromG") ? Float.parseFloat(params.get("fromG").toString()) : 0f;
                float fb = params.containsKey("fromB") ? Float.parseFloat(params.get("fromB").toString()) : 0f;

                float tr = params.containsKey("toR") ? Float.parseFloat(params.get("toR").toString()) : 0f;
                float tg = params.containsKey("toG") ? Float.parseFloat(params.get("toG").toString()) : 1f;
                float tb = params.containsKey("toB") ? Float.parseFloat(params.get("toB").toString()) : 0f;

                float size = params.containsKey("size") ? Float.parseFloat(params.get("size").toString()) : 1f;

                data = new Particle.DustTransition(
                        Color.fromRGB((int)(fr * 255), (int)(fg * 255), (int)(fb * 255)),
                        Color.fromRGB((int)(tr * 255), (int)(tg * 255), (int)(tb * 255)),
                        size
                );
            }

            else if(particle == Particle.SCULK_CHARGE)
            {
                float roll = params.containsKey("roll") ? Float.parseFloat(params.get("roll").toString()) : 0f;
                data = roll;
            }

            else if(particle == Particle.SHRIEK)
            {
                int delay = params.containsKey("delay") ? Integer.parseInt(params.get("delay").toString()) : 0;
                data = delay;
            }

            else if(particle == Particle.VIBRATION)
            {
                double dx = params.containsKey("destX") ? Double.parseDouble(params.get("destX").toString()) : x;
                double dy = params.containsKey("destY") ? Double.parseDouble(params.get("destY").toString()) : y;
                double dz = params.containsKey("destZ") ? Double.parseDouble(params.get("destZ").toString()) : z;

                int ticks = params.containsKey("ticks") ? Integer.parseInt(params.get("ticks").toString()) : 20;

                Location dest = new Location(player.getWorld(), dx, dy, dz);

                data = new Vibration(
                        new Vibration.Destination.BlockDestination(dest),
                        ticks
                );
            }

            else if (particle == Particle.BLOCK || particle == Particle.BLOCK_CRUMBLE) {
                String blockName = params.containsKey("block") ? params.get("block").toString() : "stone";
                Material mat = Material.valueOf(blockName.toUpperCase());
                data = mat.createBlockData();
            }

            else if (particle == Particle.ITEM) {
                String itemName = params.containsKey("item") ? params.get("item").toString() : "stone";
                Material mat = Material.valueOf(itemName.toUpperCase());
                data = new ItemStack(mat);
            }

            if (data != null) {
                InventoryALaCarte.singleton.getLogger().info("[Particle] Spawning " + particle + " at " + loc + " with data: " + data);
                player.getWorld().spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, data);
            } else {
                InventoryALaCarte.singleton.getLogger().info("[Particle] Spawning " + particle + " at " + loc);
                player.getWorld().spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ);
            }

        } catch (Exception e) {
            InventoryALaCarte.singleton.getLogger().warning("[MenuLoader] Error while spawning particle: " + e.getMessage());
        }
    }
}
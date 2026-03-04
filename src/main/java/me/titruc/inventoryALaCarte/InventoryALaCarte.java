package me.titruc.inventoryALaCarte;

import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryALaCarte extends JavaPlugin {

    public static JavaPlugin singleton;

    @Override
    public void onEnable() {

        singleton = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

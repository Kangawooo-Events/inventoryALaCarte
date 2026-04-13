package me.titruc.inventoryALaCarte;

import me.titruc.inventoryALaCarte.listener.MenuListener;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableCondition;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableCondition.IsGameMode;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableCondition.IsOpCondition;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent.*;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import me.titruc.inventoryALaCarte.register.ItemRegistry;
import me.titruc.inventoryALaCarte.register.Register;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.plugin.java.JavaPlugin;


public final class InventoryALaCarte extends JavaPlugin {

    //singleton
    public static JavaPlugin singleton;
    //config
    public static FileConfiguration config;
    //configHandler (ty cadden)
    public static ConfigHandler configHandler;

    //register for clickable event and menu
    public static final Register<MenuHolder> menuRegister = new Register<>();
    public static final Register<ClickableEvent> clickableEventRegister = new Register<ClickableEvent>();
    public static final Register<ClickableCondition> clickableConditionRegister = new Register<>();
    public static final ItemRegistry itemRegistry = new ItemRegistry();

    @Override
    public void onEnable() {

        //easier reference to plugin
        singleton = this;

        // save default config
        saveDefaultConfig();

        // update config singleton
        config = getConfig();

        // reload ConfigHandler
        ConfigHandler.refresh();

        //Listener
        getServer().getPluginManager().registerEvents(new MenuListener(),this);

        //register built in clickable event and condition
        registerBuiltInClickableEvent();
        registerBuiltInClickableCondition();
    }

    @Override
    public void reloadConfig()
    {
        //do normal reload stuff
        super.reloadConfig();
        // update config singleton
        config = getConfig();
        //reload the config api
        ConfigHandler.refresh();
    }

    private void registerBuiltInClickableEvent()
    {
        clickableEventRegister.register("close", new CloseMenuEvent());
        clickableEventRegister.register("send_message", new SendMessage());
        clickableEventRegister.register("give_item_from_material", new GiveItemFromMaterial());
        clickableEventRegister.register("spawn_entity_at", new SpawnEntity());
        clickableEventRegister.register("spawn_particle_at_location", new SpawnParticleAtPosition());
        clickableEventRegister.register("spawn_particle_at_player", new SpawnParticleAtPlayer());
        clickableEventRegister.register("play_sound_at", new PlaySoundAt());
        clickableEventRegister.register("play_sound_to_player", new PlaySoundToPlayer());
        clickableEventRegister.register("give_item_from_registry", new GiveItemFromRegistry());
    }

    private void registerBuiltInClickableCondition()
    {
        clickableConditionRegister.register("is_op", new IsOpCondition());
        clickableConditionRegister.register("is_in_gamemode", new IsGameMode());
    }
}

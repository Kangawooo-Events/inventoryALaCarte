package me.titruc.inventoryALaCarte;

import me.titruc.inventoryALaCarte.listener.MenuListener;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableCondition;
import me.titruc.inventoryALaCarte.menu.clickableEvent.ClickableEvent;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableCondition.IsOpCondition;
import me.titruc.inventoryALaCarte.menu.clickableEvent.builtinClickableEvent.CloseMenuEvent;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import me.titruc.inventoryALaCarte.register.Register;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class InventoryALaCarte extends JavaPlugin {

    //singleton
    public static JavaPlugin singleton;
    //config
    public static FileConfiguration config;
    //configHandler (ty cadden)
    public static ConfigHandler configHandler;

    //register for clickable event and menu
    public static final Register<MenuHolder> menuRegister = new Register<>();
    public static final Register<ClickableEvent> clickableEventRegister = new Register<>();
    public static final Register<ClickableCondition> clickableConditionRegister = new Register<>();

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
    }

    private void registerBuiltInClickableCondition()
    {
        clickableConditionRegister.register("is_op", new IsOpCondition());
    }
}

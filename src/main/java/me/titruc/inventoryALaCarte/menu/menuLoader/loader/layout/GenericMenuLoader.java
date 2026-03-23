package me.titruc.inventoryALaCarte.menu.menuLoader.loader.layout;

import me.titruc.inventoryALaCarte.menu.menuLoader.loader.MenuLoader;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import me.titruc.inventoryALaCarte.menu.menuUI.layout.MenuGeneric;
import org.bukkit.configuration.file.FileConfiguration;

public class GenericMenuLoader extends MenuLoader
{

    @Override
    protected MenuHolder createMenu(FileConfiguration config)
    {
        int rows = config.getInt("rows", 3);

        return new MenuGeneric(rows);
    }

}

package me.titruc.inventoryALaCarte.menu.menuLoader;

import me.titruc.inventoryALaCarte.menu.menuLoader.loader.MenuLoader;
import me.titruc.inventoryALaCarte.menu.menuLoader.loader.layout.GenericMenuLoader;
import me.titruc.inventoryALaCarte.menu.menuLoader.loader.layout.MerchantMenuLoader;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;


public class MenuFactory {

    public static MenuHolder createFromFile(File file) {

        if (!file.exists()) return null;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String type = config.getString("type");
        if (type == null) return null;

        MenuLoader loader;
        switch (type) {
            case "generic" :
                loader = new GenericMenuLoader();
                break;
            case "merchant" :
                loader = new MerchantMenuLoader();
                break;
            default :
                return null;
        }

        return loader.load(config);
    }

    public static MenuHolder createFromPath(String path)
    {
        File file = new File(path);
        return createFromFile(file);
    }

}

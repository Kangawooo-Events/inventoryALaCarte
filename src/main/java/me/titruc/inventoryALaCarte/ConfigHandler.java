package me.titruc.inventoryALaCarte;
import static me.titruc.inventoryALaCarte.InventoryALaCarte.config;

public class ConfigHandler
{
    //Persistent data name
    public static String defaultMenuName;
    public static Boolean defaultMenuBold;
    public static int defaultGenericMenuRow;


    public static void refresh()
    {
        defaultMenuName = config.getString("menu_settings/default_name");
        defaultMenuBold = config.getBoolean("menu_settings/default_bold");
        defaultGenericMenuRow = config.getInt("menu_settings/default_generic_row");
    }

}

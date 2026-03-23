package me.titruc.inventoryALaCarte.menu.menuLoader.loader;

import me.titruc.inventoryALaCarte.ConfigHandler;
import me.titruc.inventoryALaCarte.menu.menuUI.MenuHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuLoader
{
    public MenuHolder load(FileConfiguration config)
    {
        ConfigurationSection title = config.getConfigurationSection("title");
        if(title == null)
        {
            return null;
        }
        //create menu
        MenuHolder menu = createMenu(config);
        //set title
        menu.setTitle(getTitle(title));
        //load item in menu
        loadItems(menu, config);

        return menu;
    }

    private Component getTitle(ConfigurationSection title)
    {
        String titleString  = title.getString("title", ConfigHandler.defaultMenuName);
        return Component.text(titleString);
    }

    protected abstract MenuHolder createMenu(FileConfiguration config);

    protected void loadItems(MenuHolder menu, FileConfiguration config) {

        if(!config.contains("items")) return;


        for(Map<?, ?> rawItem : config.getMapList("items")) {

            int slot = (int) rawItem.get("slot");

            Material material = Material.valueOf((String) rawItem.get("material"));

            ItemStack item = new ItemStack(material);

            ItemMeta meta = item.getItemMeta();

            if(rawItem.containsKey("name")) meta.displayName(Component.text((String) rawItem.get("name")));
            if(rawItem.containsKey("lore")) meta.lore(((List<String>) rawItem.get("lore")).stream().map(Component::text).toList());



            if (rawItem.containsKey("click")) {
                Object rawClick = rawItem.get("click");

                if (rawClick instanceof Map<?, ?> clickMapRaw) {

                    Map<String, Object> clickMap = new HashMap<>();
                    for (Map.Entry<?, ?> e : clickMapRaw.entrySet()) {
                        clickMap.put(e.getKey().toString(), e.getValue());
                    }

                    String clickableEventName = (String) clickMap.get("action");

                    Map<String, Object> parameter =
                            getParameterMap(clickMap.get("parameter"));

                    Map<String, Map<String, Object>> rawConditions =
                            getConditionsMap(clickMap.get("conditions"));

                    menu.addMenuAction(slot, clickableEventName, parameter, rawConditions);
                }

                else if (rawClick instanceof List<?> clicksRaw) {

                    for (Object entry : clicksRaw) {
                        if (!(entry instanceof Map<?, ?> clickMapRaw)) continue;

                        Map<String, Object> clickMap = new HashMap<>();
                        for (Map.Entry<?, ?> e : clickMapRaw.entrySet()) {
                            clickMap.put(e.getKey().toString(), e.getValue());
                        }

                        String clickableEventName = (String) clickMap.get("action");

                        Map<String, Object> parameter =
                                getParameterMap(clickMap.get("parameter"));

                        Map<String, Map<String, Object>> rawConditions =
                                getConditionsMap(clickMap.get("conditions"));

                        menu.addMenuAction(slot, clickableEventName, parameter, rawConditions);
                    }
                }
            }

            item.setItemMeta(meta);
            menu.getInventory().setItem(slot, item);
        }
    }

    public static Map<String, Object> getParameterMap(Object paramObj) {
        Map<String, Object> paramsMap = new HashMap<>();

        if (paramObj == null) return paramsMap;

        if (paramObj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                paramsMap.put(e.getKey().toString(), e.getValue());
            }
        }

        else if (paramObj instanceof List<?> list) {
            for (Object entry : list) {
                if (entry instanceof Map<?, ?> mapEntry) {
                    for (Map.Entry<?, ?> e : mapEntry.entrySet()) {
                        paramsMap.put(e.getKey().toString(), e.getValue());
                    }
                }
            }
        }

        return paramsMap;
    }

    public static Map<String, Map<String, Object>> getConditionsMap(Object conditionObj) {
        Map<String, Map<String, Object>> conditions = new HashMap<>();

        if (conditionObj == null) return conditions;

        if (conditionObj instanceof List<?> list) {
            for (Object entry : list) {
                if (!(entry instanceof Map<?, ?> mapEntry)) continue;

                Map<String, Object> conditionArgs = new HashMap<>();
                String type = null;

                for (Map.Entry<?, ?> e : mapEntry.entrySet()) {
                    String key = e.getKey().toString();

                    if (key.equals("type")) {
                        type = e.getValue().toString();
                    } else {
                        conditionArgs.put(key, e.getValue());
                    }
                }

                if (type != null) {
                    conditions.put(type, conditionArgs);
                }
            }
        }

        else if (conditionObj instanceof Map<?, ?> mapEntry) {
            Map<String, Object> conditionArgs = new HashMap<>();
            String type = null;

            for (Map.Entry<?, ?> e : mapEntry.entrySet()) {
                String key = e.getKey().toString();

                if (key.equals("type")) {
                    type = e.getValue().toString();
                } else {
                    conditionArgs.put(key, e.getValue());
                }
            }

            if (type != null) {
                conditions.put(type, conditionArgs);
            }
        }

        return conditions;
    }
}

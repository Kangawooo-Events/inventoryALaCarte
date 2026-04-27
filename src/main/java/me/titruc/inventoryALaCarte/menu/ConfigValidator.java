package me.titruc.inventoryALaCarte.menu;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public class ConfigValidator {

    private final ConfigurationSection config;
    private final String context;
    private boolean isValid = true;

    private ConfigValidator(ConfigurationSection config, String context) {
        this.config = config;
        this.context = context;
    }

    public static ConfigValidator of(ConfigurationSection config, String context) {
        return new ConfigValidator(config, context);
    }

    public ConfigValidator required(String field) {
        if(!config.contains(field))
        {
            severe("field is missing and is required : " + field, context);
            isValid = false;
        }
        return this;
    }

    public ConfigValidator required(String field, String message) {
        if(!config.contains(field))
        {
            severe(message, context);
            isValid = false;
        }
        return this;
    }

    public ConfigValidator ignored(String field) {
        if(config.contains(field))
        {
            warn("field is here but will be ignored : " + field, context);
        }
        return this;
    }

    public ConfigValidator ignored(String field, String message) {
        if(config.contains(field))
        {
            warn(message, context);
        }
        return this;
    }

    public ConfigValidator requiredNonEmpty(String field){

        if (!config.contains(field)) {
            severe("field is missing (so/and is empty list) : " + field);
            isValid = false;
            return this;
        }

        List<Map<?, ?>> dataList = config.getMapList(field);
        if (dataList.isEmpty()) {
            severe("list is empty where it shouldn't : " + field, context);
            isValid = false;
        }

        return this;
    }

    public ConfigValidator requiredNonEmpty(String field, String message){

        if (!config.contains(field)) {
            severe("field don't exist ");
            isValid = false;
            return this;
        }

        List<Map<?, ?>> dataList = config.getMapList(field);
        if (dataList.isEmpty()) {
            severe(message, context);
            isValid = false;
        }

        return this;
    }

    public boolean validate(){
        return isValid;
    }

    //logger methods can be use anywhere
    static private void warn(String message)
    {
        InventoryALaCarte.singleton.getLogger().warning(message);
    }

    static private void warn(String message, String context)
    {
        InventoryALaCarte.singleton.getLogger().warning("[" + context + "] " + message);
    }

    static private void severe(String message)
    {
        InventoryALaCarte.singleton.getLogger().severe(message);
    }

    static private void severe(String message, String context)
    {
        InventoryALaCarte.singleton.getLogger().severe("[" + context + "] " + message);
    }
}

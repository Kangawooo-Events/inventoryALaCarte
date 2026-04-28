package me.titruc.inventoryALaCarte.menu.validator;

import me.titruc.inventoryALaCarte.InventoryALaCarte;
import me.titruc.inventoryALaCarte.menu.validator.template.ConfigValidatorConfigurationSection;
import me.titruc.inventoryALaCarte.menu.validator.template.ConfigValidatorMap;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.Map;

public abstract class ConfigValidator {

    protected final String context;
    protected boolean isValid = true;

    protected ConfigValidator(String context) {
        this.context = context;
    }

    public static ConfigValidator of(ConfigurationSection config, String context) {
        return new ConfigValidatorConfigurationSection(config, context);
    }

    public static ConfigValidator of(Map<?,?> map, String context) {
        return new ConfigValidatorMap(map, context);
    }

    protected abstract boolean contains(String field);

    public ConfigValidator required(String field) {
        if(!contains(field))
        {
            severe("field is missing and is required : " + field, context);
            isValid = false;
        }
        return this;
    }

    public ConfigValidator required(String field, String message) {
        if(!contains(field))
        {
            severe(message, context);
            isValid = false;
        }
        return this;
    }

    public ConfigValidator ignored(String field) {
        if(contains(field))
        {
            warn("field is here but will be ignored : " + field, context);
        }
        return this;
    }

    public ConfigValidator ignored(String field, String message) {
        if(contains(field))
        {
            warn(message, context);
        }
        return this;
    }

    public ConfigValidator requiredNonEmpty(String field){

        return this;
    }

    public ConfigValidator requiredNonEmpty(String field, String message){

        return this;
    }

    public ConfigValidator requireOneOf(String... fields)
    {
        boolean hasOne = false;

        int i  = 0;
        while(!hasOne && i < fields.length)
        {
            hasOne = contains(fields[i]);
            i++;
        }

        if(!hasOne)
        {
            severe("one of those fields should be present : " + Arrays.toString(fields), context);
            isValid = false;
        }

        return this;
    }

    public ConfigValidator requireOneOfWithMessage(String message, String... fields)
    {
        boolean hasOne = false;

        int i  = 0;
        while(!hasOne && i < fields.length)
        {
            hasOne = contains(fields[i]);
            i++;
        }

        if(!hasOne)
        {
            severe(message, context);
            isValid = false;
        }

        return this;
    }

    public boolean validate(){
        return isValid;
    }

    //logger methods can be use anywhere
    static public void warn(String message)
    {
        InventoryALaCarte.singleton.getLogger().warning(message);
    }

    static public void warn(String message, String context)
    {
        InventoryALaCarte.singleton.getLogger().warning("[" + context + "] " + message);
    }

    static public void severe(String message)
    {
        InventoryALaCarte.singleton.getLogger().severe(message);
    }

    static public void severe(String message, String context)
    {
        InventoryALaCarte.singleton.getLogger().severe("[" + context + "] " + message);
    }
}

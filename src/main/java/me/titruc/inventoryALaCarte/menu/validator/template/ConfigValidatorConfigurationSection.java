package me.titruc.inventoryALaCarte.menu.validator.template;

import me.titruc.inventoryALaCarte.menu.validator.ConfigValidator;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public class ConfigValidatorConfigurationSection extends ConfigValidator {

    private final ConfigurationSection config;

    public ConfigValidatorConfigurationSection(ConfigurationSection config, String context) {
        super(context);
        this.config = config;
    }

    @Override
    protected boolean contains(String field) {
        return config.contains(field);
    }

    @Override
    public ConfigValidator requiredNonEmpty(String field){

        if (!contains(field)) {
            severe("field is missing (so/and is empty list) : " + field, context);
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

    @Override
    public ConfigValidator requiredNonEmpty(String field, String message){

        if (!config.contains(field)) {
            severe("field don't exist ", context);
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
}

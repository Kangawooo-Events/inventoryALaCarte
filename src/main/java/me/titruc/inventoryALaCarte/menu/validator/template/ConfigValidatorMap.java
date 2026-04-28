package me.titruc.inventoryALaCarte.menu.validator.template;

import me.titruc.inventoryALaCarte.menu.validator.ConfigValidator;

import java.util.Map;

public class ConfigValidatorMap extends ConfigValidator {

    private final Map<?,?> map;

    public ConfigValidatorMap(Map<?, ?> map, String context) {
        super(context);
        this.map = map;
    }

    @Override
    protected boolean contains(String field) {
        return map.containsKey(field);
    }


}
